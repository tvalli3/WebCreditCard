/*  This page does all the magic for applying
 *  Ajax to a DebitCard history scenario.
 *  Script will return data in HTML format.
 */
 
// Have a function run after the page loads:
var ajax;
window.onload = init;

// Function that adds the Ajax layer:
function init() {

  // Get an XMLHttpRequest object:
  ajax = getXMLHttpRequestObject();
  
  if (ajax) {
        // Function that handles the response:
        ajax.onreadystatechange = function() {
            handleResponse();
        }
  }
} // End of init() function.

// Function that handles the response from the servlet script:
function handleResponse() {

  // Check that the transaction is complete:
  if (ajax.readyState === 4) {
  
    // Check for a valid HTTP status code:
    if ((ajax.status === 200) || (ajax.status === 304) ) {
      
      // Put the received response in the DOM:
      var results = document.getElementById('results');
      results.innerHTML = ajax.responseText;
    } else { // Bad status code, submit the form normally
      document.getElementById('card').submit();
    }
  } // End of readyState IF.
}