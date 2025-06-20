## Room Locking Example

This repo is a minimal Android project that demonstrates database contention issues.

- the database & related components are defined in the `db` package
  - there's a table/entity defined (`ExampleEntity`), but currently no data is actually read or written from it
- all of the interesting demonstration logic is in `ExampleViewModel`
- any sql that is run is output to logcat under tag `SQL`
