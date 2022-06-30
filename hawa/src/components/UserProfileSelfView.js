import { useState, useContext, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import {findByUsername, logout } from '../services/authApi';
import AuthContext from '../contexts/AuthContext';
import './UserProfileSelfView.css';
import Footer from './Footer';


function UserProfileSelfView(){

const [user, setUser]=useState([]);

const auth = useContext(AuthContext);
const [errors, setErrors] = useState([]);

useEffect(() => {
   fetchUser();
  }, []);

const fetchUser=()=>{
  findByUsername(auth.user.username)
  .then(data => {
    setUser(data);
  })
  .catch(err => setErrors(err));
};
    return <>

 

<div class="relative max-w-md mx-auto md:max-w-2xl mt-6 min-w-0 break-words bg-white w-full mb-6 shadow-lg rounded-xl mt-16">
    <div class="px-6">
        <div class="flex flex-wrap justify-center">
            <div class="w-full flex justify-center">
                <div class="relative">
                    <img src={user.profile_picture} class="shadow-xl rounded-full align-middle border-none absolute -m-16 -ml-20 lg:-ml-16 max-w-[150px]"/>
                </div>
            </div>
            <div class="w-full text-center mt-20">
                <div class="flex justify-center lg:pt-4 pt-8 pb-0">
                    <div class="p-3 text-center">
                        <span class="text-xl font-bold block uppercase tracking-wide text-slate-700">3,360</span>
                        <span class="text-sm text-slate-400">Photos</span>
                    </div>
                    <div class="p-3 text-center">
                        <span class="text-xl font-bold block uppercase tracking-wide text-slate-700">2,454</span>
                        <span class="text-sm text-slate-400">Followers</span>
                    </div>

                    <div class="p-3 text-center">
                        <span class="text-xl font-bold block uppercase tracking-wide text-slate-700">564</span>
                        <span class="text-sm text-slate-400">Following</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-center mt-2">
            <h3 class="text-2xl text-slate-700 font-bold leading-normal mb-1">{user.first_name} {user.last_name}</h3>
            <div class="text-xs mt-0 mb-2 text-slate-400 font-bold uppercase">
                <i class="fas fa-at"></i>{user.username}
            </div>
        </div>
        <div class="mt-6 py-6 border-t border-slate-200 text-center">
            <div class="flex flex-wrap justify-center">
                <div class="w-full px-4">
                    <p class="font-light leading-relaxed text-slate-600 mb-4">An artist of considerable range, Mike is the name taken by Melbourne-raised, Brooklyn-based Nick Murphy writes, performs and records all of his own music, giving it a warm.</p>
                    {/* <a href="javascript:;" class="font-normal text-slate-700 hover:text-slate-400">Edit</a> */}
                    <button className='btn info'><Link to="/editUserProfile">Edit</Link></button>
                </div>
            </div>
        </div>
    </div>
</div>


<Footer/>

    </>

}
export default UserProfileSelfView;