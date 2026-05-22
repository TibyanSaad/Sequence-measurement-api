# Change Log
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


