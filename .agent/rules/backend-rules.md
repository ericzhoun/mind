1- Prefer fully typed functions; add type hints to all new or modified code.
2- Use typing primitives or pydantic‑style dicts only when unavoidable.
3- Catch narrow, specific exceptions and never expose stack traces to users.
4- Log errors with structured extra={...}, including context and durations.
5- Follow a strict logging schema: lower_snake event names plus task/session/user IDs.
6- Wrap all network/LLM calls with timeouts and log start/success/failure events.
7- Ensure deterministic test behavior by mocking external services (Ollama, encode, network,…).
8- Use pytest ecosystem (pytest, pytest-django, pytest-asyncio) with network blocked by default.
9- Add regression tests for any new retrieval or pipeline logic.
10-Run pre-commit (black, isort, ruff, mypy) before pushing; keep line length ≤100.
11-Fix ruff warnings or explicitly annotate why they’re ignored.
12-Include migrations for any model/schema changes and document required backfills.
13-Update docs/ADRs whenever architecture or retrieval logic changes.
14-Keep logs and tests green; avoid adding new network dependencies in unit tests.
15-Follow the Senior Architect persona: professional, rigorous, test‑first, long‑term maintainability.
16-Use the mandated workflow: Blueprint → Test‑Driven Implementation → Optimization → Verification.
17-Deliverables must follow the required structure: approach → tests → implementation → verification checklist.
18-Enforce deterministic folder and module structure so the AI never invents new directories or naming patterns.
19-Require every generated function/class to include a docstring explaining intent, assumptions, and edge‑case behavior.
20-Mandate that all AI‑generated code paths include predictable return types—no implicit None or mixed return shapes.
21-Disallow hidden side effects; all state changes must be explicit, logged, and test‑covered.
22-Require the agent to surface architectural decisions (e.g., why a pattern was chosen) in the PR description.
23-Enforce security hygiene: no hardcoded secrets, no inline credentials, and no writing to unexpected file paths.
24-Require the AI to validate inputs and sanitize external data before processing.
25-Disallow “creative rewrites” of core logic unless explicitly requested—stability > novelty.
26-Require the agent to reuse existing utilities/helpers instead of generating new ones with similar behavior.
27-Enforce idempotency for all ingestion, retrieval, and pipeline steps to avoid duplicate work or inconsistent state.
28-Require the agent to log all long‑running operations with duration and correlation IDs for traceability.
29-Disallow adding new dependencies without justification and documentation of impact.
30-Require every new feature to include a rollback plan or safe‑failure mode.
31-Enforce that all AI‑generated code must pass static analysis (mypy, ruff) before being accepted.
32-Require the agent to reference existing patterns, naming conventions, and architectural decisions before generating
new code.
33-Require the agent to generate TODO comment only when accompanied by a clear explanation and follow-up task
