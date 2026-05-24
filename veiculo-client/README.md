# REST Monolith Client (Angular)

Cliente Angular para todos os controllers REST do monolito Spring Boot Erudio.

## Executar

```bash
cd veiculo-client
npm install
npm start
```

- Frontend: `http://localhost:4200`
- API: `http://localhost:8080` (configurável em `src/environments/environment.ts`)

## Navegação

- **Início** — painel principal com cards de todos os controllers
- **Abas superiores** — uma aba por controller da API
- **Abas internas** (em cada painel) — uma sub-aba por endpoint

## Controllers cobertos

| Aba | Base path |
|-----|-----------|
| Veículo | `/veiculo` |
| Book | `/book` |
| PC | `/pc` |
| Person v1 | `/person/v1` |
| Person v2 | `/person/v2` |
| Files v1 | `/api/files/v1` |
| Files v2 | `/api/files/v2` |
| Jasper Reports | `/person/v2/reports/jasper` |
| Math | `/math` |
| Greeting | `/greeting` |
| Test Log | `/log` |
