import { Injectable } from '@angular/core';

interface Captcha {
  captchaId: number; path: string; value: string
}

@Injectable()
export class CaptchaService {

  private selectedCaptcha: Captcha;

  private captchaData: Captcha[] = [{
    captchaId: 1,
    path: "assets/resources/Captcha_images/captcha1.png",
    value: "12345"
  },
  {
    captchaId: 2,
    path: "assets/resources/Captcha_images/captcha2.png",
    value: "736829"
  },
  {
    captchaId: 3,
    path: "assets/resources/Captcha_images/captcha3.png",
    value: "ACMODJ"
  },
  {
    captchaId: 4,
    path: "assets/resources/Captcha_images/captcha4.png",
    value: "LSJHK"
  },
  {
    captchaId: 5,
    path: "assets/resources/Captcha_images/captcha5.png",
    value: "KNonsK"
  },
  ]

  constructor() { }

  selectCaptchaRandom(): string {
    let captchaId = Math.ceil(Math.random() * this.captchaData.length);
    this.selectedCaptcha = this.captchaData.filter((captcha) => captcha.captchaId == captchaId)[0];
    return this.selectedCaptcha.path;
  }

  getSelectedCaptchaImagePath(): string {
    return this.selectedCaptcha.path;
  }

  checkCaptha(value: string): boolean {
    return this.selectedCaptcha.value == value;
  }

}
