export interface PersonV1 {
  id?: number;
  first_name?: string;
  last_name?: string;
  birthday?: string;
  address?: string;
  gender?: string;
  phoneNumber?: string;
  email?: string;
  enabled?: boolean;
  profileUrl?: string;
  photoUrl?: string;
}

export interface PersonV2 {
  id?: number;
  firstName?: string;
  lastName?: string;
  address?: string;
  gender?: string;
  birthday?: string;
  enabled?: boolean;
  profileUrl?: string;
  photoUrl?: string;
  books?: string[];
}

export interface PageParams {
  page?: number;
  size?: number;
  direction?: string;
  properties?: string;
  sort?: string;
}
