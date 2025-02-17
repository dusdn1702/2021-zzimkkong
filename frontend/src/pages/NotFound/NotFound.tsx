import NotFoundImage from 'assets/images/notFound.png';
import Header from 'components/Header/Header';
import Layout from 'components/Layout/Layout';
import PATH from 'constants/path';
import * as Styled from './NotFound.styles';

const Main = (): JSX.Element => {
  return (
    <>
      <Header />
      <Layout>
        <Styled.Container>
          <Styled.Image src={NotFoundImage} alt="Not Found" />
          <Styled.PageHeader>해당 페이지를 찾지 못했습니다.</Styled.PageHeader>
          <Styled.HomeLink to={PATH.MANAGER_MAIN}>홈으로 이동하기</Styled.HomeLink>
        </Styled.Container>
      </Layout>
    </>
  );
};

export default Main;
