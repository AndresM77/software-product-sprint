// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function fetchComments() {
  fetch('/data').then(response => response.json()).then((commentData) => {
    
    //Test console log statements
    console.log("Hi")
    console.log(commentData);
    
    //Loops through elements of json object to get every comment
    const comments = document.getElementById('comment-container');
    comments.innerHTML = '';
    for(i = 0; i < commentData.length; i++) {
         comments.appendChild(
         createListElement('Name: ' + commentData[i].author));
         comments.appendChild(
         createListElement('Message: ' + commentData[i].message));
         comments.appendChild(
         createListElement('Email: ' + commentData[i].email));
    }
    });
}

function fetchLogin() {
    fetch('/login').then(response => response.text()).then((loginData) => {
        const login_div = document.getElementById('login-container');
        login_div.innerHTML = loginData;
    });
}

function fetchStatus() {
    fetch('/status').then(response => response.text()).then((status) => {
        str1 = "true";
        if (str1.localeCompare(status.trim()) == 0) {
            document.getElementById("comment-form").style.display = "block";
        } else {
            document.getElementById("comment-form").style.display = "none";
        }
    });
    
    fetchLogin();
    fetchComments();
}
    
/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}


/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['The passage of time is an illusion and life is a mausoleum',
       'Better to write for yourself and have no public than to write for the public and have no self',
        'Nothing happens unless first a dream',
        'Every failure is another step towards success'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}
