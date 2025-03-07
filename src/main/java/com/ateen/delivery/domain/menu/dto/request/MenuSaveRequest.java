package com.ateen.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuSaveRequest {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Size(min = 1, max = 20, message = "메뉴 이름은 1 ~ 20자여야 합니다.")
    private String name;

    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    private Integer price;

    @Size(max = 100, message = "설명은 100자 이내여야 합니다.")
    private String detail;
}
