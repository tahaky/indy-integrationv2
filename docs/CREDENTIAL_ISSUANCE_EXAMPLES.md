# Automated Credential Issuance Examples

This document provides examples of using the automated credential issuance endpoint.

## Basic Usage

Issue a credential with simple attributes:

```bash
curl -X POST "http://localhost:8080/v1/credentials/v2/issue" \
  -H "Content-Type: application/json" \
  -d '{
    "connection_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "cred_def_id": "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag",
    "attributes": {
      "name": "John Doe",
      "age": "30",
      "email": "john@example.com"
    }
  }'
```

## With Control Flows

Issue a credential with approval flow (holder must accept):

```bash
curl -X POST "http://localhost:8080/v1/credentials/v2/issue" \
  -H "Content-Type: application/json" \
  -d '{
    "connection_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "cred_def_id": "WgWxqztrNooG92RXvxSTWv:3:CL:20:tag",
    "attributes": {
      "full_name": "Alice Smith",
      "employee_id": "EMP-12345",
      "department": "Engineering",
      "start_date": "2024-01-15"
    },
    "comment": "Employee identity credential",
    "auto_issue": false,
    "auto_remove": true,
    "trace": false
  }'
```

## Complex Credential Schema

The endpoint works with any credential schema. Example with more attributes:

```bash
curl -X POST "http://localhost:8080/v1/credentials/v2/issue" \
  -H "Content-Type: application/json" \
  -d '{
    "connection_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "cred_def_id": "WgWxqztrNooG92RXvxSTWv:3:CL:30:degree",
    "attributes": {
      "student_name": "Bob Johnson",
      "student_id": "STU-67890",
      "degree": "Bachelor of Science",
      "major": "Computer Science",
      "graduation_date": "2024-05-15",
      "gpa": "3.85",
      "honors": "Magna Cum Laude",
      "institution": "University of Technology"
    },
    "comment": "Degree credential for graduation 2024",
    "auto_issue": true,
    "auto_remove": false
  }'
```

## Response Example

All requests return a similar response structure:

```json
{
  "cred_ex_id": "v2-12345678-90ab-cdef-1234-567890abcdef",
  "connection_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "state": "offer-sent",
  "thread_id": "thread-98765432-10ab-cdef-5678-90abcdef1234",
  "created_at": "2026-01-15T22:00:00.000Z"
}
```

## State Flow

The credential exchange goes through these states:

1. `offer-sent` - Initial state after sending the offer
2. `request-received` - Holder accepted and sent credential request
3. `credential-issued` - Credential issued to holder
4. `done` - Exchange completed successfully

You can track the state by querying:
```bash
GET /v1/credentials/v2/records/{cred_ex_id}
```

## Error Handling

The endpoint validates all required fields:
- `connection_id` - must be provided
- `cred_def_id` - must be provided
- `attributes` - must be provided (can be empty map)

Invalid requests return 400 Bad Request with error details.
