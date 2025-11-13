import React, { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';

import '../css/index/indexPage.css';


// custom 3D Object 
let custom3dLoader = new GLTFLoader();



// if you need You can load DynamicLoad GLTFLoader 
//  import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';  

// FireBase Setting Simuller REact...
const appId = typeof __app_id !== 'undefined' ? __app_id : 'default-app-id';
// const firebaseConfig = typeof __firebase_config !== 'undefined' ? JSON.parse(__firebase_config) : {};
// const initialAuthToken = typeof __initial_auth_token !== 'undefined' ? __initial_auth_token : null;


const IndexPage = () => {

    const mountRef = useRef(null);
    const reqIdRef = useRef();
    const[isLoading, setIsLoading] = useState(true);

    useEffect(()=>{

        if(!mountRef.current) return;

        let camera, scene, renderer, cube;
        const container = mountRef.current;

        // Scene init 
        const width = container.clientWidth;
        const height = container.clientHeight;

        try{
            // Scene Setting 
            scene = new THREE.Scene();
            scene.background = new THREE.Color(0xE16E01);

            // Camera Setting 
            camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
            camera.position.z = 3; 

            // renderer Setting 
            renderer = new THREE.WebGLRenderer({ antialias:true, alpha: true});
            renderer.setSize(width, height);

            // CSS defin Container => Add Canvers
            container.appendChild(renderer.domElement);

            // Object 
            const geometry = new THREE.BoxGeometry(2,2,2);


            const material = new THREE.MeshStandardMaterial({
                color: 0xd1982e,
                metalness: 0.6,
                roughness: 0.5,
                transparent: true,
                opacity: 0.3,
            });
            cube = new THREE.Mesh(geometry, material);
            scene.add(cube);

            // Add light 
            const ambientLight = new THREE.AmbientLight(0xffffff, 0.7);
            scene.add(ambientLight);

            // DirectionLight => for metalness, roughtness
            const directionLight = new THREE.DirectionalLight(0xffffff, 1);
            directionLight.position.set(2,1,2).normalize();
            scene.add(directionLight);

            const directionLight2 = new THREE.DirectionalLight(0x00ffff, 1);
            directionLight2.position.set(2,-2,-2).normalize();
            scene.add(directionLight2);

            setIsLoading(false); // loading

            // Animation Loop
            const ani =() =>{
                reqIdRef.current =requestAnimationFrame(ani);

                // Cube Run
                if(cube){
                    cube.rotation.x += 0.038;
                    cube.rotation.y += 0.028;
                }

                renderer.render(scene, camera);
            };

            // WindowSize Change Handler 
            const onWidowResize =()=>{

                // get Parent current Window Size 
                const currentWidth = container.clientWidth;
                const currentHeight = container.clientHeight;

                camera.aspect = currentWidth / currentHeight;
                camera.updateProjectionMatrix();
                
                renderer.setSize(currentWidth, currentHeight);
                renderer.render(scene, camera);
            };

            window.addEventListener('resize', onWidowResize, false);
            ani();

            // ClentUp (if Unmount)
            return () => {
                cancelAnimationFrame(reqIdRef.current);
                window.removeEventListener('resize', onWidowResize, false);
                container.removeChild(renderer.domElement);
            };
        } catch(error) {
            console.error("Three.js init ERROR", error);
            setIsLoading(false);
        }
    }, []);
  return (

     <div className="index">
              <div ref={mountRef} 
               
                className="flex-grow w-full max-w-4xl rounded-xl shadow-2xl bg-gray-800/50 
                           flex justify-center items-center "
                style={{ height: '100vh' }} // 헤더/푸터 공간 제외
            >
                {isLoading && (
                    <div className="flex flex-col items-center text-teal-400">
                        <div className="w-10 h-10 animate-spin" />
                        <span className="mt-3">3D 씬 로딩 중...</span>
                    </div>
                )}
                {/* Three.js Canvers Insert HERE  */}
            </div>
                <div className="index-title">
                    <h1> 러닝크루 project</h1>
                </div>
                <div className="index-con">
                    <Link to={'/shop'} className="text-draw"
                        data-text="S H O P">:: ENTER ::</Link>
                </div>
            </div>
  )
}

export default IndexPage