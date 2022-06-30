import { useState, useEffect,useContext } from 'react';
import { Link, useParams, useHistory } from 'react-router-dom';
import { findByUsername, updateUser } from '../services/authApi';
import Errors from './Errors';
import AuthContext from '../contexts/AuthContext';

const EMPTY_USER = {

    "app_user_id": 0,
    "username": "",
    "password": "",
    "email": "",
    "first_name":"",
    "last_name":"",
    "birthday":"",
    "relation_status":"",
    "gender":"",
    "profile_picture":""
};
function UserProfileEdit(){
  const auth = useContext(AuthContext);
  const {username } = useParams();
  const [user, setUser] = useState([]);
  const [errors, setErrors] = useState([]);
  const history = useHistory();

  const fetchUser=()=>{
    findByUsername(auth.user.username)
    .then(data => {
      setUser(data);
    })
    .catch(err => setErrors(err));
  };

  useEffect(() => {
    fetchUser();
    if (username) {
      findByUsername(username)
        .then(data => setUser(data))
        .catch(err => setErrors(err));
    }
  }, [username]);

  const handleChange = (evt) => {
    const nextUser = { ...user };
    let nextValue = evt.target.value;
    nextUser[evt.target.name] = nextValue;
    setUser(nextUser);
  }

  const handleSubmit = (evt) => {
    evt.preventDefault();

     updateUser(user)
      .then(() => {
        history.push('/home');
      })
      .catch(err => {
        setErrors([...err]);
      });
  };

    return <>
  

  <div class="min-w-screen min-h-screen bg-gray-900 flex items-center justify-center px-5 py-5">
    <div class="bg-gray-100 text-gray-500 rounded-3xl shadow-xl w-full overflow-hidden" >
        <div class="md:flex w-full">
            <div class="hidden md:block w-1/2 bg-white-500 py-10 px-10">
                
            <img src={user.profile_picture}></img>
            </div>
            <div class="w-full md:w-1/2 py-10 px-5 md:px-10">
                <div class="text-center mb-10">
                    <h1 class="font-bold text-3xl text-gray-900">Edit Profile</h1>
                    <p>Enter your information to update your profile</p>
                </div>
                <div>
                    <div class="flex -mx-3">
                        <div class="w-1/2 px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">First name</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-user"></i></div>
                                <input type="text" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg
                                 border-2 border-gray-200 outline-none focus:border-indigo-500" value={user.first_name}></input>
                            </div>
                        </div>
                        <div class="w-1/2 px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">Last name</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-user"></i></div>
                                <input type="text" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg
                                 border-2 border-gray-200 outline-none focus:border-indigo-500" value={user.last_name}></input>
                            </div>
                        </div>
                    </div>
                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">Email</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-envelope"></i></div>
                                <input type="email" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2
                                 border-gray-200 outline-none focus:border-indigo-500" value={user.email}></input>
                            </div>
                        </div>
                    </div>

                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">Username</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-id-card"></i></div>
                                <input type="email" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2
                                 border-gray-200 outline-none focus:border-indigo-500" value={user.username}></input>
                            </div>
                        </div>
                    </div>

                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">Relationship Status</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-user-friends"></i></div>
                                <input type="email" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2
                                 border-gray-200 outline-none focus:border-indigo-500" value={user.relation_status}></input>
                            </div>
                        </div>
                    </div>

                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-5">
                            <label for="" class="text-xs font-semibold px-1">Bio Description</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-keyboard	"></i></div>
                                <textarea type="email" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg border-2
                                 border-gray-200 outline-none focus:border-indigo-500" value={user.bio}></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-12">
                            <label for="" class="text-xs font-semibold px-1">Password</label>
                            <div class="flex">
                                <div class="w-10 z-10 pl-1 text-center pointer-events-none flex items-center justify-center">
                                <i class="fas fa-lock"></i></div>
                                <input type="password" class="w-full -ml-10 pl-10 pr-3 py-2 rounded-lg 
                                border-2 border-gray-200 outline-none focus:border-indigo-500"></input>
                            </div>
                        </div>
                    </div>

                    
                    <div class="flex -mx-3">
                        <div class="w-full px-3 mb-5">
                            <button class="block w-full max-w-xs mx-auto bg-red-500 hover:bg-gray-700 
                            focus:bg-indigo-700 text-white rounded-lg px-3 py-3 font-semibold"><Link to="/home">Cancel</Link></button>
                        </div>
                        <div class="w-full px-3 mb-5">
                            <button class="block w-full max-w-xs mx-auto bg-blue-500 hover:bg-green-700 
                            focus:bg-indigo-700 text-white rounded-lg px-3 py-3 font-semibold" type='submit'>Save</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</div>


    </>
}
export default UserProfileEdit;