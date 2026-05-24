export interface Veiculo {
  id?: number;
  placa: string;
  marca: string;
  modelo: string;
  ano: string;
  cor: string;
}

export interface ApiError {
  timestamp?: string;
  message?: string;
  details?: string;
}
