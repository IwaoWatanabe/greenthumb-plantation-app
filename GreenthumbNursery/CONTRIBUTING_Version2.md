```markdown
# Contributing to Greenthumb Nursery

Thanks for your interest in contributing! This document explains how to set up the project locally, run tests, and submit changes.

Getting started (local)
1. Fork the repository and clone your fork:
   git clone git@github.com:<your-username>/greenthumb-nursery.git
   cd greenthumb-nursery

2. Create a local configuration file:
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   Edit the file and set DB credentials (do NOT commit the edited file).

3. Build the project:
   mvn clean package

4. Run tests:
   mvn test

Branching & commits
- Use a topic branch for each change:
  git checkout -b feat/short-description
- Write clear, focused commits. Use Conventional Commits:
  - feat: new feature
  - fix: bug fix
  - chore: maintenance
  - docs: documentation
  - test: tests
  - style: formatting

Pull requests
- Push your branch to your fork and open a Pull Request against the main branch.
- Describe what the change does, why it’s needed, and include screenshots if the UI changed.
- If your PR changes DB schema, include migration steps or SQL scripts.

Code style
- Follow standard Java conventions and document public classes/methods with Javadoc.
- Keep methods small and single-purpose.
- Use PreparedStatement for database queries to avoid SQL injection.

Tests
- Add unit tests for new logic. Use JUnit 5.
- Aim for deterministic, fast tests that do not depend on external services. Use in-memory DB or Testcontainers for integration tests.

Security & secrets
- Do not commit secrets or passwords.
- Use src/main/resources/application.properties.example as the template for local configuration.
- If you find a security vulnerability, see SECURITY.md for responsible disclosure.

Reporting issues
- Use the GitHub Issues tab and pick an appropriate template.
```