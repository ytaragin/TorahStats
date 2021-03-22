/**
 * @license
 * Copyright Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// [START sheets_quickstart]
const fs = require('fs');
const readline = require('readline');
const {google} = require('googleapis');

// If modifying these scopes, delete token.json.
const SCOPES = ['https://www.googleapis.com/auth/spreadsheets'];
// The file token.json stores the user's access and refresh tokens, and is
// created automatically when the authorization flow completes for the first
// time.
const TOKEN_PATH = 'token.json';

// Load client secrets from a local file.
fs.readFile('credentials.json', (err, content) => {
  if (err) return console.log('Error loading client secret file:', err);
  // Authorize a client with credentials, then call the Google Sheets API.
  authorize(JSON.parse(content), writeData); //listMajors);
});

/**
 * Create an OAuth2 client with the given credentials, and then execute the
 * given callback function.
 * @param {Object} credentials The authorization client credentials.
 * @param {function} callback The callback to call with the authorized client.
 */
function authorize(credentials, callback) {
  const {client_secret, client_id, redirect_uris} = credentials.installed;
  const oAuth2Client = new google.auth.OAuth2(
      client_id, client_secret, redirect_uris[0]);

  // Check if we have previously stored a token.
  fs.readFile(TOKEN_PATH, (err, token) => {
    if (err) return getNewToken(oAuth2Client, callback);
    oAuth2Client.setCredentials(JSON.parse(token));
    callback(oAuth2Client);
  });
}

/**
 * Get and store new token after prompting for user authorization, and then
 * execute the given callback with the authorized OAuth2 client.
 * @param {google.auth.OAuth2} oAuth2Client The OAuth2 client to get token for.
 * @param {getEventsCallback} callback The callback for the authorized client.
 */
function getNewToken(oAuth2Client, callback) {
  const authUrl = oAuth2Client.generateAuthUrl({
    access_type: 'offline',
    scope: SCOPES,
  });
  console.log('Authorize this app by visiting this url:', authUrl);
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  rl.question('Enter the code from that page here: ', (code) => {
    rl.close();
    oAuth2Client.getToken(code, (err, token) => {
      if (err) return console.error('Error while trying to retrieve access token', err);
      oAuth2Client.setCredentials(token);
      // Store the token to disk for later program executions
      fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
        if (err) return console.error(err);
        console.log('Token stored to', TOKEN_PATH);
      });
      callback(oAuth2Client);
    });
  });
}

function numToLetter(num) {
  return String.fromCharCode(64+num)
}

function getEndCol(colCount) {
  let pred = '';

  let prednum = Math.floor(colCount/26);
  let remain = colCount%26;
  console.log(`${colCount} -> prednum=${prednum} remain=${remain}`)

  if (prednum > 0) {
      pred =numToLetter(prednum);
  }
  return `${pred}${numToLetter(remain)}`
}  

function uploadFile(auth, spreadsheetId, values, range, valueInputOption) {
  // const spreadsheetId = '1xWI0LE2ePaBf-zRzZSYhEttARLrhEhZGFldp5inSZIE';
   const sheets = google.sheets({version: 'v4', auth});
 
   const resource = {
     values,
   };
 
   sheets.spreadsheets.values.update({
     spreadsheetId,
     range,
     valueInputOption,
     resource,
   }, (err, result) => {
     if (err) {
       // Handle error
       console.log(err);
     } else {
       console.log(`Result:`, result.updatedCells);
     }
   });
 
}


function saveToSheets(auth, filename, sheetName, spreadsheetId) {
  let inlines = fs.readFileSync(filename).toString().split("\n");
  let data = inlines.map(i => i.split(','));
  
  let colCount = data[0].length
  
 
  let range = `'${sheetName}'!A1:${getEndCol(colCount)}${data.length+1}`;

  console.log(`Writing ${data.length} rows and ${colCount} to ${range}`);

  uploadFile(auth, spreadsheetId, data, range, "USER_ENTERED"); //RAW or USER_ENTERED	

  console.log("Complete");

}



function writeData(auth) {


  const spreadsheetId = '19InWBAbauTBjj6x32bevyvt6dJz6_I9uz55KwF91NnE';
  
  saveToSheets(auth, '../TorahBreakdown.csv', 'Torah Breakdown', spreadsheetId);
  saveToSheets(auth, '../ParshaBreakdown.csv', 'Parsha Breakdown', spreadsheetId);
  saveToSheets(auth, '../AliyahBreakdown.csv', 'Aliyah Breakdown', spreadsheetId);
  saveToSheets(auth, '../PerekBreakdown.csv', 'Perek Breakdown', spreadsheetId);

  
}


module.exports = {
  SCOPES,
};
