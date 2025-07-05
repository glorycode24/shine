# Frontend Integration Debug Guide

## Current Issue: 403 Forbidden Error

The 403 Forbidden error indicates that the JWT token is not being properly processed by the backend.

## Steps to Debug

### 1. **Restart Backend**
First, restart your Spring Boot application to apply the security configuration changes:

```bash
# Stop the current backend
# Then restart it
./mvnw spring-boot:run
```

### 2. **Test JWT Token Validity**
Use the new test endpoint to verify your JWT token is working:

```bash
curl -X GET http://localhost:8080/api/auth/test-auth \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

Replace `YOUR_JWT_TOKEN_HERE` with the actual token from your frontend logs.

### 3. **Check Backend Logs**
Look for these debug messages in your backend console:

```
JWT Filter - Request URI: /api/cart/items
JWT Filter - Authorization header: present
JWT Filter - Extracted email: jrfg092000@gmail.com
JWT Filter - Authentication successful for user: jrfg092000@gmail.com
```

### 4. **Frontend Token Debugging**
Add this to your frontend to debug token issues:

```javascript
// In your CartService or wherever you make API calls
const token = localStorage.getItem('jwt_token');
console.log('Token being sent:', token);

// Check if token is expired
if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiration = new Date(payload.exp * 1000);
    console.log('Token expires at:', expiration);
    console.log('Current time:', new Date());
    console.log('Token expired:', expiration < new Date());
}
```

### 5. **Common Issues and Solutions**

#### Issue 1: Token Expired
**Symptoms:** 403 error, token expired message in logs
**Solution:** Re-login to get a new token

#### Issue 2: CORS Issues
**Symptoms:** 403 error, no Authorization header in backend logs
**Solution:** Check that your frontend is sending the Authorization header correctly

#### Issue 3: User Not Found
**Symptoms:** 403 error, "User not found" in backend logs
**Solution:** Verify the user exists in the database

#### Issue 4: Role Issues
**Symptoms:** 403 error, authentication successful but authorization failed
**Solution:** Check user role in database

### 6. **Test Cart Endpoint Directly**

Test the cart endpoint with curl to isolate the issue:

```bash
# Test without token (should fail)
curl -X GET http://localhost:8080/api/cart/items

# Test with token (should work)
curl -X GET http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### 7. **Frontend Integration Checklist**

- [ ] JWT token is stored after login
- [ ] Token is included in Authorization header
- [ ] Token is not expired
- [ ] Backend is running on correct port (8080)
- [ ] CORS is properly configured
- [ ] User exists in database with correct role

### 8. **Quick Fixes to Try**

#### Fix 1: Clear and Re-login
```javascript
// Clear stored token
localStorage.removeItem('jwt_token');
// Re-login to get fresh token
```

#### Fix 2: Check Token Format
Make sure your token is being sent as:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Fix 3: Verify Backend URL
Ensure your frontend is calling the correct backend URL:
```javascript
const cartService = new CartService('http://localhost:8080');
```

### 9. **Expected Behavior After Fix**

After the security configuration is applied:

1. **Without token:** 401 Unauthorized
2. **With valid token:** 200 OK with cart data
3. **With expired token:** 401 Unauthorized
4. **With invalid token:** 401 Unauthorized

### 10. **Next Steps**

1. Restart your backend
2. Test the `/api/auth/test-auth` endpoint
3. Check backend logs for JWT filter messages
4. Update your frontend to handle authentication errors properly

## Still Having Issues?

If you're still getting 403 errors after following these steps:

1. Check the backend logs for specific error messages
2. Verify the JWT token format and expiration
3. Test with a simple curl command first
4. Ensure the user exists in the database with the correct role

Let me know what you find in the backend logs and I can help further! 