# Change Log

## Version[0.0.2] - 21-05-2026
### Fixed
- User input is converted to lower case.
- Handled invalid user input (numbers and special characters).

## Version[0.0.1] - 21-05-2026
### Added
- Initial release of Sequence Measurement API.
- Core algorithm for decoding measurement sequences ("SequenceService") with z-multiplier encoding.
- Spring Boot server with `/convert-measurements` endpoint returning decoded sums.
- Sequence model with auto-generated ID and timestamp.
- Application startup check in `SequenceMeasurementApiApplicationTests.java`.


