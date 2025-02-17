import { ReactNode, useState } from 'react';
import MapDefault from 'assets/images/map-default.jpg';
import * as Styled from './MapListItem.styles';

export interface Props {
  thumbnail: {
    src: string;
    alt: string;
  };
  title: string;
  control?: ReactNode;
  selected?: boolean;
  onClick?: () => void;
}

const MapListItem = ({
  thumbnail,
  title,
  control,
  selected = false,
  onClick = () => null,
}: Props): JSX.Element => {
  const [thumbnailSrc, setThumbnailSrc] = useState(thumbnail.src);

  const onImgError = () => {
    setThumbnailSrc(MapDefault);
  };

  return (
    <Styled.Container role="listitem" selected={selected}>
      <Styled.ImageWrapper onClick={onClick}>
        <Styled.ImageInner>
          <Styled.Image
            src={thumbnailSrc}
            alt={thumbnail.alt}
            onError={onImgError}
            loading="lazy"
          />
        </Styled.ImageInner>
      </Styled.ImageWrapper>
      <Styled.TitleWrapper>
        <Styled.Title>{title}</Styled.Title>
        {control && <Styled.Control>{control}</Styled.Control>}
      </Styled.TitleWrapper>
    </Styled.Container>
  );
};

export default MapListItem;
