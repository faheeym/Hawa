
import { useState, useContext, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { authenticate, register } from '../services/authApi';
import AuthContext from '../contexts/AuthContext';
import Errors from './Errors';
import './Login.css';

function Login(){
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
      });    
      const [errors, setErrors] = useState([]);
      const auth = useContext(AuthContext);
      const history = useHistory();
    
    
      const handleChange = (evt) => {
        const nextCredentials = { ...credentials };
        nextCredentials[evt.target.name] = evt.target.value;
        setCredentials(nextCredentials);
      };
    
      const handleSubmit = (evt) => {
        evt.preventDefault();
        authenticate(credentials)
          .then(data => {
            auth.onAuthenticated(data);
            history.push('/home');
          })
          .catch(err => setErrors(err));

      };

      const handleSubmitRegister = (evt) => {
        evt.preventDefault();
          register(credentials)
          .then(data => {
            history.push('/');
          })
          .catch(err => setErrors(err));
      };

useEffect(() => {

const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

  sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});


  return () => {
   
    sign_up_btn.removeEventListener("click", () => {
      container.classList.add("sign-up-mode");
    });
    
    sign_in_btn.removeEventListener("click", () => {
      container.classList.remove("sign-up-mode");
    });
  };
}, []);

return (
    <>

    <div class="container">
      <div class="forms-container">
        <div class="signin-signup">
          <form action="#" class="sign-in-form" onSubmit={handleSubmit}>
            <h2 class="title">Log  in to Hawa</h2>
            <div class="input-field">
              <i class="fas fa-user"></i>
              <input type="text" placeholder="Username" 
              id="username"
              name="username"
              autoComplete="username"
              value={credentials.username} onChange={handleChange}/>
            </div>
            <div class="input-field">
              <i class="fas fa-lock"></i>
              <input type="password" placeholder="Password" 
              
              id="password"
                  name="password"
                  autoComplete="current-password"
                  required
                  value={credentials.password} onChange={handleChange}
                  />
            </div>
            <input type="submit" value="Login" class="btn solid" />
            <p class="social-text">Or Sign in with social platforms</p>
            <div class="social-media">
              <a href="#" class="social-icon">
                <i class="fab fa-facebook-f"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-twitter"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-google"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-linkedin-in"></i>
              </a>
            </div>
           <div>
           <Errors errors={errors} />
           </div>
          </form>

          <form action="#" class="sign-up-form" onSubmit={handleSubmitRegister}>
            <h2 class="title">Sign up</h2>

            <div className="-mx-2 md:flex">
              <div className="input-field2">
              <i class="fas fa-user"></i>
                <input
                  placeholder="First Name"
                  id="first_name"
                  type="text"
                  name="first_name"
                  value={credentials.first_name}
                  onChange={handleChange}
                ></input>
              </div>

              <div className="input-field2">
              <i class="fas fa-user"></i>
                <input
                  placeholder="Last Name"
                  id="last_name"
                  type="text"
                  name="last_name"
                  value={credentials.last_name}
                  onChange={handleChange}
                ></input>
              </div>

           
            </div>

            <div class="input-field">
            <i class="fas fa-envelope"></i>
              <input type="text" placeholder="Email" 
              id="email"
              name="email"
              autoComplete="email"
              required
              value={credentials.email} onChange={handleChange}
              />
            </div>
            
            <div class="input-field">
            <i class="fas fa-id-badge"></i>
              <input type="text" placeholder="Username" 
              id="username"
              name="username"
              autoComplete="username"
              required
              value={credentials.username} onChange={handleChange}
              />
            </div>
          
            <div class="input-field">
              <i class="fas fa-lock"></i>
              <input type="password" placeholder="Password" 
              id="password"
              name="password"
              
              required
              value={credentials.password} onChange={handleChange}
              />
            </div>
            <div class="input-field">
              <i class="fas fa-calendar"></i>
              <input type="date" placeholder="Birthday" 
              id="birthday"
              name="birthday"
              required
              value={credentials.birthday} onChange={handleChange}/>
            </div>
            <div class="input-field" >
            
            <i class="fa fa-venus-mars" ></i>
              <select class="input-field1"
              id="gender"
              name="gender"
              required
              value={credentials.gender} onChange={handleChange}>
              <option disabled selected>Gender</option>
              <option>Male</option>
              <option>Female</option>
              <option>Other</option>
              </select>
            </div>

         

            <input type="submit" class="btn" value="Sign up" />
            <p class="social-text">Or Sign up with social platforms</p>
            <div class="social-media">
              <a href="#" class="social-icon">
                <i class="fab fa-facebook-f"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-twitter"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-google"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-linkedin-in"></i>
              </a>
            </div>
            
          </form>
          <div>
           <Errors errors={errors} />
           </div>
        </div>
      </div>

      <div class="panels-container">
        <div class="panel left-panel">
          <div class="content">
            <h3>New to Hawa ?</h3>
            <p>
              Don't have an account with us yet? Let's get you on board and connect with the world!
            </p>
            <button class="btn transparent" id="sign-up-btn">
              Sign up
            </button>
          </div>
          <img src="img/log.svg" class="image" alt="" />
        </div>
        <div class="panel right-panel">
          <div class="content">
            <h3>Have An Account ?</h3>
            <p>
              Already have an account? Sign in now and start making stories and connect with the world.
            </p>
            <button class="btn transparent" id="sign-in-btn">
              Sign in
            </button>
          </div>
          <img src="img/register.svg" class="image" alt="" />
        </div>
      </div>


    </div>

    </>
    
  )
}
export default Login;