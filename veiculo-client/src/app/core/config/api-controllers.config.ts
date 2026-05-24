export interface ApiControllerMeta {
  id: string;
  label: string;
  basePath: string;
  description: string;
  endpointCount: number;
}

export const API_CONTROLLERS: ApiControllerMeta[] = [
  {
    id: 'veiculo',
    label: 'Veículo',
    basePath: '/veiculo',
    description: 'CRUD de veículos',
    endpointCount: 5,
  },
  {
    id: 'book',
    label: 'Book',
    basePath: '/book',
    description: 'Livros com negociação de conteúdo',
    endpointCount: 6,
  },
  {
    id: 'pc',
    label: 'PC',
    basePath: '/pc',
    description: 'Configurações de computador',
    endpointCount: 5,
  },
  {
    id: 'person-v1',
    label: 'Person v1',
    basePath: '/person/v1',
    description: 'Pessoas, paginação, importação e exportação',
    endpointCount: 9,
  },
  {
    id: 'person-v2',
    label: 'Person v2',
    basePath: '/person/v2',
    description: 'Pessoas v2 com HATEOAS e mass create',
    endpointCount: 8,
  },
  {
    id: 'files-v1',
    label: 'Files v1',
    basePath: '/api/files/v1',
    description: 'Upload e download de arquivos',
    endpointCount: 3,
  },
  {
    id: 'files-v2',
    label: 'Files v2',
    basePath: '/api/files/v2',
    description: 'Upload e download (storage v2)',
    endpointCount: 3,
  },
  {
    id: 'jasper',
    label: 'Jasper Reports',
    basePath: '/person/v2/reports/jasper',
    description: 'Relatórios PDF e XLSX',
    endpointCount: 2,
  },
  {
    id: 'math',
    label: 'Math',
    basePath: '/math',
    description: 'Operações matemáticas',
    endpointCount: 5,
  },
  {
    id: 'greeting',
    label: 'Greeting',
    basePath: '/greeting',
    description: 'Endpoint de saudação',
    endpointCount: 1,
  },
  {
    id: 'log',
    label: 'Test Log',
    basePath: '/log',
    description: 'Geração de logs de teste',
    endpointCount: 1,
  },
];
