import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination, Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import React from "react";

const ImageSlider = ({ images }) => {
  return (
    <Swiper
      spaceBetween={30}
      centeredSlides={true}
      autoplay={{
        deplay: 2500,
        disableOnInteraction: false,
      }}
      pagination={{
        clickable: true,
      }}
      navigation={true}
      modules={[Autoplay, Pagination, Navigation]}
      className="storeSwiper"
    >
      {images.map((image, index) => {
        return (
          <SwiperSlide key={index}>
            <img
              src={image.src}
              alt={image.src}
              tyle={{ width: "auto", height: "auto" }}
            />
          </SwiperSlide>
        );
      })}
    </Swiper>
  );
};

export default ImageSlider;
