const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const FaviconsWebpackPlugin = require('favicons-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');

module.exports = () => {
  const isDevelopment = process.env.NODE_ENV !== 'production';

  return {
    mode: isDevelopment ? 'development' : 'production',
    devtool: isDevelopment ? 'eval-source-map' : 'source-map',
    entry: './src/index.tsx',
    output: {
      publicPath: '/',
      filename: 'bundle.js',
      path: path.resolve(__dirname, 'dist'),
      clean: true,
    },
    resolve: {
      extensions: ['.ts', '.tsx', '.js', '.jsx'],
      plugins: [new TsconfigPathsPlugin({})],
    },
    devServer: {
      port: 3000,
      open: true,
      hot: true,
      historyApiFallback: true,
    },
    module: {
      rules: [
        {
          test: /\.(ts|tsx)$/,
          use: {
            loader: 'ts-loader',
            options: {
              configFile: path.resolve(__dirname, 'tsconfig.json'),
            },
          },
          exclude: /node_modules/,
        },
        {
          test: /\.svg$/,
          use: [
            {
              loader: '@svgr/webpack',
              options: {
                svgoConfig: {
                  plugins: {
                    removeViewBox: false,
                  },
                },
              },
            },
            'url-loader',
          ],
        },
        {
          test: /\.(png|jpg)$/,
          type: 'asset/resource',
        },
      ],
    },
    plugins: [
      new HtmlWebpackPlugin({
        template: 'public/index.html',
      }),
      new FaviconsWebpackPlugin({
        logo: 'src/assets/images/logo.png',
        publicPath: '/',
        manifest: 'public/manifest.json',
      }),
    ],
  };
};
