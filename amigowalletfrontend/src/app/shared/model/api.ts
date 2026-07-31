import { UserProfile } from './user';

/** Response of POST /UserLoginAPI/authenticate. */
export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserProfile;
}

/** Generic `{ message }` envelope many endpoints return. */
export interface MessageResponse {
  message?: string;
}

/** Body of the registration endpoints (validateForRegistration / register). */
export interface RegisterRequest {
  name: string;
  emailId: string;
  mobileNumber: string;
  password: string;
  securityQuestion?: { questionId: number };
  securityAnswer?: string;
}

/** Response of POST /ForgotPasswordAPI/forgotPassword. */
export interface ForgotPasswordResponse {
  questionId: number;
  question: string;
}

/** Response of POST /ForgotPasswordAPI/validateAnswer. */
export interface ResetTokenResponse {
  resetToken: string;
  expiresIn: number;
}

/** Credentials submitted on login / register forms. */
export interface Credentials {
  emailId: string;
  password: string;
}
