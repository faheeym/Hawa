import Login from "./components/Login";
import Footer from "./components/Footer";
import { logout, refreshToken } from "./services/authApi";
import { useEffect, useState } from "react";
import { Switch, Route } from "react-router-dom";
import AuthContext from "./contexts/AuthContext";
import UserProfileSelfView from "./components/UserProfileSelfView";
import UserProfileEdit from "./components/UserProfileEdit";
const REFRESH_TIME = 14 * 60 * 1000;
const EMPTY_USER = {
  username: "",
  roles: [],
};
function App() {

  const [user, setUser] = useState(EMPTY_USER);

  const auth = {
    user: user,
    onAuthenticated(authenticatedUser) {
      const nextUser = { ...user };
      nextUser.username = authenticatedUser.username;
      nextUser.roles = authenticatedUser.roles;
      setUser(nextUser);
      setTimeout(refresh, REFRESH_TIME);
    },
    hasRole(role) {
      return user.roles.includes(role);
    },
    signOut() {
      setUser(EMPTY_USER);
      logout();
    },
  };

  useEffect(() => {
    refresh();
  }, []);

  const refresh = () => {
    refreshToken()
      .then((data) => {
        auth.onAuthenticated(data);
      })
      .catch((err) => {});
  };

  return (
    <>

<AuthContext.Provider value={auth}>

<Switch>
<Route exact path="/">
            <Login />
          </Route>
<Route exact path="/home">
  <UserProfileSelfView/>
</Route>

<Route  path="/EditUserProfile">
  <UserProfileEdit/>
</Route>

</Switch>

</AuthContext.Provider>




    </>
  );
}

export default App;
