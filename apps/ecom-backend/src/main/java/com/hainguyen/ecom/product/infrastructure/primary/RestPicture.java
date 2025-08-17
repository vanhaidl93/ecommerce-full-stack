package com.hainguyen.ecom.product.infrastructure.primary;

import com.hainguyen.ecom.product.domain.aggregate.Picture;
import com.hainguyen.ecom.product.domain.aggregate.PictureBuilder;
import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record RestPicture(byte[] file,
                          String mimeType) {

  public RestPicture{
    Assert.notNull("file",file);
    Assert.notNull("mimeType",mimeType);
  }

  public static Picture to(RestPicture restPicture){
    return PictureBuilder.picture()
      .file(restPicture.file())
      .mimeType(restPicture.mimeType())
      .build();
  }

  public static RestPicture from(Picture picture){
    return RestPictureBuilder.restPicture()
      .file(picture.file())
      .mimeType(picture.mimeType())
      .build();
  }

  public static Set<Picture> to(Set<RestPicture> restPictures){
    return restPictures.stream().map(RestPicture::to).collect(Collectors.toSet());
  }

  public static Set<RestPicture> from(Set<Picture> restPictures){
    return restPictures.stream().map(RestPicture::from).collect(Collectors.toSet());
  }
}
