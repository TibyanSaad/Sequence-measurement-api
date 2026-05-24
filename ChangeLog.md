# Change Log
## Version[0.0.6] - 24-05-2026
### Fixed
- `Sequence.java`: removed all JPA annotations, now a pure domain object. `value` field changed from comma-separated `String` to `List<String>` directly. Added `setId()` and `setTimestamp()` setters.
- `SequenceHistory.java`: new class that takes over all JPA annotations and DB mapping from `Sequence`. Handles comma-separated conversion of `value` via `fromSequence()` and `toSequence()` methods.
- `SequenceRepo.java`: updated to use `SequenceHistory` instead of `Sequence` as the JPA entity.
- `SequenceService.java`: added `saveAndReturn()` helper to handle `Sequence` ↔ `SequenceHistory` conversion on every save. Added `getAllHistory()` for raw DB access. Added `MAX_Z_CHAIN = 100` constant and full edge case handling in `computeValues()`.
- `SequenceController.java`: added `GET /convert-measurements/history` endpoint that returns raw `SequenceHistory` records from the DB.

## Version[0.0.5] - 22-05-2026
### Fixed
- User is able to check all the history records of previous requests.
- Added GET request query for specific IDs.

## Version[0.0.4] - 22-05-2026
### Fixed
- Added `SequenceRepo` interface for DB connection.
- Established a connection with the DB and tested with multiple requests.
- PUT request now works with raw JSON format to update input value.

## Version[0.0.3] - 21-05-2026
### Fixed
- Added PUT request to edit using input with endpoint `/convert-measurements/{id}`
- Added DELETE request with endpoint `/convert-measurements/{id}`.
- fixed error related to getting source IP rather than ServerSocket used HttpServletRequest.

## Version[0.0.2] - 21-05-2026
### Fixed
- User input is converted to lower case.
- Handled invalid user input (numbers and special characters).

## Version[0.0.1] - 20-05-2026
### Added
- Initial release of Sequence Measurement API.
- Core algorithm for decoding measurement sequences ("SequenceService") with z-multiplier encoding.
- Spring Boot server with `/convert-measurements` endpoint returning decoded sums.
- Sequence model with auto-generated ID and timestamp.
- Application startup check in `SequenceMeasurementApiApplicationTests.java`.


