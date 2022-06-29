const url = "http://localhost:8080/api/userprofile";


const getToken = () => {
    return localStorage.getItem("jwt_token");
  };
  
  export async function findByUsername(username) {
    const response = await fetch(`${url}/${username}`);
    if (response.status === 200) {
      return await response.json();
    } else {
      return Promise.reject(["Unable to fetch User"]);
    }
  }
  
  export async function addMovie(user) {
    const token = getToken();
    if (!token) {
      return Promise.reject(["Forbidden"]);
    }
  
    const init = {
      method: "POST",
      body: JSON.stringify(user),
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    const response = await fetch(url, init);
    if (response.status === 201) {
      return await response.json();
    } else if (response.status === 400) {
      const errors = await response.json();
      return Promise.reject(errors);
    } else {
      return Promise.reject(["Unable to add user details"]);
    }
  }
  
  export async function updateMovie(user) {
    const token = getToken();
    if (!token) {
      return Promise.reject(["Forbidden"]);
    }
  
    const init = {
      method: "PUT",
      body: JSON.stringify(user),
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    };
    const response = await fetch(`${url}/${user.user_profile_id}`, init);
    if (response.status === 400) {
      const errors = await response.json();
      return Promise.reject(errors);
    } else if (response.status === 409) {
      return Promise.reject(["You are updating the user!"]);
    } else if (response.status === 404) {
      return Promise.reject(["Unable to find user"]);
    } else if (response.status !== 200) {
      return Promise.reject(["Unable to update user details"]);
    }
  }
  
  