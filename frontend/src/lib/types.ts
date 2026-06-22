export type Role = "CLIENT" | "PROVIDER" | "ADMIN";

export type JobStatus = "PENDING" | "ACCEPTED" | "COMPLETED" | "CANCELLED";

export interface ApiResponse<T> {
  message?: string;
  data: T;
  success?: boolean;
}

export interface RegisterRequest {
  fullName: string;
  phoneNumber: string;
  password: string;
  city: string;
}

export interface RegisterClient extends RegisterRequest {}

export interface RegisterProvider extends RegisterRequest {
  business_name: string;
  category: string;
  bio?: string;
  years_of_experience?: number;
  avatar?: File | null;
}

export interface LoginRequest {
  number: string;
  password: string;
}

export interface UserDTO {
  user_id: string;
  fullName: string;
  phone: string;
  role: Role;
  city: string;
  createdAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: UserDTO;
}

export interface CategoryDTO {
  id: string;
  nameEn: string;
  nameFr: string;
  nameAr: string;
  isActive: boolean;
  createdAt: string;
}

export interface ProviderSummaryDTO {
  id: string;
  fullName: string;
  businessName: string;
  category: CategoryDTO | null;
  averageRating: number;
  isVerified: boolean;
  avatarUrl: string | null;
}

export interface ProviderDTO extends UserDTO {
  providerId: string;
  businessName: string;
  category: CategoryDTO | null;
  bio: string | null;
  yearsOfExperience: number;
  isVerified: boolean;
  averageRating: number;
  totalReviews: number;
  avatarUrl: string | null;
}

export interface JobPendingDTO {
  id: string;
  clientId: string;
  clientName: string;
  description: string;
  status: JobStatus;
  createdAt: string;
}

export interface JobNotCompletedDTO extends JobPendingDTO {
  providerId: string;
  providerName: string;
}

export interface JobCompletedDTO extends JobNotCompletedDTO {
  completedAt: string;
}

export type JobDTO = JobPendingDTO | JobNotCompletedDTO | JobCompletedDTO;

export interface CreateJobRequest {
  description: string;
}

export interface ReviewResponseDTO {
  id: string;
  reviewerName: string;
  reviewedName: string;
  ratingOverall: number;
  comment: string;
  createdAt: string;
}

export interface ClientReviewDTO extends ReviewResponseDTO {
  ratingPayment: number;
  ratingRespect: number;
}

export interface ProviderReviewDTO extends ReviewResponseDTO {
  ratingQuality: number;
  ratingPunctuality: number;
  ratingCommunication: number;
  ratingPriceFairness: number;
}

export interface CreateReviewDTO {
  jobId: string;
  reviewedId: string;
  reviewerType: Role;
  comment: string;
  ratingQuality?: number;
  ratingPunctuality?: number;
  ratingCommunication?: number;
  ratingPriceFairness?: number;
  ratingPayment?: number;
  ratingRespect?: number;
}

export interface CallRequest {
  clientId: string;
  providerId: string;
}

export interface CallResponse {
  token: string;
  url: string;
}

export interface CreateCategoryRequest {
  nameAr: string;
  nameFr: string;
  nameEn: string;
}
