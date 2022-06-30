const url = "http://localhost:8080";

export async function findByUsername(username) {
  const response = await fetch(`${url}/${username}`);
  if (response.status === 200) {
    return await response.json();
  } else {
    return Promise.reject(["Unable to fetch user"]);
  }
}
export async function authenticate(credentials) {
  const init = {
    method: "POST",
    body: JSON.stringify(credentials),
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
  };

  const response = await fetch(`${url}/authenticate`, init);

  if (response.status === 200) {
    const result = await response.json();
    setToken(result.jwt_token);
    return makeUser(result.jwt_token);
  } else if (response.status === 403) {
    return Promise.reject(["Bad credentials"]);
  }

  return Promise.reject(["Something went wrong"]);
}

export async function register(credentials) {
  const init = {
    method: "POST",
    body: JSON.stringify(credentials),
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
  };

  const response = await fetch(`${url}/register`, init);

  if (response.status === 200) {
    const result = await response.json();
    setToken(result.jwt_token);
    return makeUser(result.jwt_token);
  } else if (response.status === 400) {
    return Promise.reject(["This User was already Registered!"]);
  }else if(response.status===500){
    return Promise.reject(["Error Creating your account. Please try a different Email Address."])
  }
}

export async function refreshToken() {
  const token = getToken();
  if (!token) {
    return Promise.reject(["Forbidden"]);
  }

  const init = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  const response = await fetch(`${url}/refresh-token`, init);
  if (response.status === 200) {
    const result = await response.json();
    setToken(result.jwt_token);
    return makeUser(result.jwt_token);
  } else if (response.status === 403) {
    return Promise.reject(["Forbidden"]);
  }

  return Promise.reject(["Something went wrong"]);
}

export function logout() {
  localStorage.removeItem("jwt_token");
}

const makeUser = (token) => {
  const decodedTokenStr = decodeToken(token);

  if (decodedTokenStr) {
    const decodedToken = JSON.parse(decodedTokenStr);
    return {
      username: decodedToken.sub,
      roles: decodedToken.roles.split(",").map((r) => r.replace("ROLE_", "")),
    };
  }
};

const decodeToken = (token) => {
  const tokenParts = token.split(".");
  if (tokenParts.length === 3) {
    return atob(tokenParts[1]);
  }
};

const setToken = (token) => {
  localStorage.setItem("jwt_token", token);
};

const getToken = () => {
  return localStorage.getItem("jwt_token");
};
