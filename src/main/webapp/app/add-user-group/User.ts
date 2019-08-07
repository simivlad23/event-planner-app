export interface User {
  id: number;
  login: String;
  firstName: String;
  lastName: String;
  email: String;
  imageUrl: String;
  activated: boolean;
  langKey: String;
  createdBy: String;
  createdDate: String;
  lastModifiedBy: String;
  lastModifiedDate: String;
  authorities: Array<String>;
}
