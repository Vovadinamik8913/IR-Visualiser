import React, {useState, useEffect} from 'react';
import Header from './components/Header';
import SVGpart from './components/SVGpart';
import TXTpart from "./components/TXTpart";
import './App.css';
import {buildByFile, buildByPath} from './components/api/build-api'
import { getFunctions, getSvgByFunction, getSvgByLine } from './components/api/svg-api';

function App() {
    const [llContent, setLlContent] = useState(null); // Содержимое .ll файла
    const [listOfFunctions, setListOfFunctions] = useState([]);
    const [svgContent, setSvgContent] = useState(''); // Содержимое SVG
    const [funcFromLine, setFuncFromLine] = useState('');
    const [irId, setIrId] = useState(0);
    const [funcName, setFuncName] = useState('');

    useEffect(() => {
        document.body.style.overflow = 'hidden';
        return () => {
          document.body.style.overflow = 'auto';
        };
      }, []);

    // Функция для загрузки .ll файла
    const handleFileUpload = (file) => {
        if (file.name.endsWith('.ll')) { // Проверка расширения
            const reader = new FileReader();
            reader.onload = (event) => {
                setLlContent(event.target.result); // Сохраняем текстовое содержимое .ll файла
                setSvgContent('');
                setListOfFunctions([]);
            };
            reader.readAsText(file);
        } else {
            alert('Пожалуйста, загрузите файл с расширением .ll');
        }
    };

    const handleBuildByFileRequest = async (folder, file) => {
        try {
            const doneRes = await buildByFile(folder, file);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
            } else {
                const id = Number(doneRes);
                console.log(id);
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    const svgsNames = await getFunctions(id);
                    setListOfFunctions(svgsNames);
                } else {
                    alert('Ошибка: не удалось получить имена функций');
                }
            }
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса "/build/file"');
        }
    };

    const handleBuildByPathRequest = async (folder, path) => {
        try {
            const doneRes = await buildByPath(folder, path);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
            } else {
                const id = Number(doneRes);
                console.log(id);
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    const svgsNames = await getFunctions(id);
                    setListOfFunctions(svgsNames);
                } else {
                    alert('Ошибка: не удалось получить имена функций');
                }
            }
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса "/build/file"');
        }
    };

    const handleGetRequest = async (funcName) => {
        try {
            const svgText = await getSvgByFunction(irId, funcName);
            setSvgContent(svgText);
            setFuncFromLine('');
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    }

    const handleLineClick = async (index) => {
        try {
            const svgName = await getSvgByLine(irId, index);
            setFuncFromLine(svgName);

            try {
                const svgText = await getSvgByFunction(svgName);
                setSvgContent(svgText);
            } catch (error) {
                console.error('Ошибка запроса:', error);
                alert('Произошла ошибка при выполнении запроса');
            }

        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    }

    return (
        <div className="App">
            <Header 
                onFileUpload={handleFileUpload}
                functions={listOfFunctions}
                funcFromLIne={funcFromLine}
                funcName={funcName}
                onBuildByFileRequest={handleBuildByFileRequest}
                onBuildByPathRequest={handleBuildByPathRequest}
            />
            <div className="main-container">
                <TXTpart
                    content={llContent}
                    onLineClick={handleLineClick}
                />
                <SVGpart    
                    functions={listOfFunctions}
                    funcFromLIne={funcFromLine}
                    setFuncName={setFuncName}
                    llContent={llContent}
                    svgContent={svgContent}
                    onGetRequest={handleGetRequest}
                />
            </div>
        </div>
    );
}

export default App;
