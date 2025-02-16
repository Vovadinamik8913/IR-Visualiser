import React, {useState, useEffect, useRef} from 'react';
import Header from './components/Header';
import SVGpart from './components/SVGpart';
import TXTpart from "./components/TXTpart";
import './App.css';
import { generateSvg, optimize, saveByFile, saveByPath} from './components/api/build-api'
import { getFunctions, getSvgByFunction, getSvgByLine } from './components/api/svg-api';
import { getCode } from './components/api/code-api';
import { getSvgWithLoops, getLoopInfo } from './components/api/loops-api';

function App() {
    const [llContent, setLlContent] = useState(null); // Содержимое .ll файла
    const [listOfFunctions, setListOfFunctions] = useState([]);
    const [svgContent, setSvgContent] = useState(''); // Содержимое SVG
    const [irId, setIrId] = useState(0);
    const [selectedFunction, setSelectedFunction] = useState('');// выбранное значение
    const [selectedOption, setSelectedOption] = useState("Анализы");
    const [loopInfo, setLoopInfo] = useState('');
    const irIdRef = useRef(irId);
    const [compilerFlags, setCompilerFlags] =  useState(''); 

    useEffect(() => {
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = 'auto';
      };
    }, []);

    useEffect(() => {
      irIdRef.current = irId
    }, [irId]);

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
            const doneRes = await saveByFile(folder, file);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
            } else {
                const id = parseInt(doneRes, 10);// Преобразуем строку в целое число
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    await generateSvg(id);
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
            const doneRes = await saveByPath(folder, path);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
            } else {
                const id = parseInt(doneRes, 10);// Преобразуем строку в целое число
                console.log(id);
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    await generateSvg(id);
                    const content = await getCode(id);
                    setLlContent(content);
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

    //получение свг(дефолт)
    const handleGetRequest = async (funcName) => {
        try {
            let svgText;
            if (selectedOption === "LoopsInfo") {
                svgText = await getSvgWithLoops(irId, funcName);
            } else {
                svgText = await getSvgByFunction(irId, funcName);
            }
            setSvgContent(svgText);
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    };

    //клик для лупса
    const handleBlockClick = async (blockTitle) => {
    
        if (selectedOption === "LoopsInfo" && selectedFunction) {
            try {
                const info = await getLoopInfo(irId, selectedFunction, blockTitle);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении информации о цикле:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
        }
    };

    //клик на строку
    const handleLineClick = async (index) => {
        try {
            const info = await getSvgByLine(irIdRef.current, index);
            setSelectedFunction(info[0]);
            try {
                const svgText = await getSvgByFunction(irIdRef.current, info[0]);
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


    const handleOptimize= async () => {
        try {
         const newId = await optimize(irId, compilerFlags);
         const id = parseInt(newId, 10);// Преобразуем строку в целое число
         if (isNaN(id)) {
             throw new Error('Сервер вернул некорректный ID');
         }
         setIrId(id);
         if (id) {
             await generateSvg(id);
             const content = await getCode(id);
             setLlContent(content);
             const svgsNames = await getFunctions(id);
             setListOfFunctions(svgsNames);
         } else {
             alert('Ошибка: не удалось получить имена функций');
         }
        } catch (error) {
          console.error('Ошибка при обработке кода:', error);
        }
    };

    const handleSelect = async(id) => {
        try {
            const content = await getCode(id);
            setLlContent(content);
            const svgsNames = await getFunctions(id);
            setListOfFunctions(svgsNames);
        } catch (error) {
            console.error('Ошибка при обработке кода:', error);
        }
    }

    return (
        <div className="App">
            <Header 
                onFileUpload={handleFileUpload}
                onBuildByFileRequest={handleBuildByFileRequest}
                onBuildByPathRequest={handleBuildByPathRequest}
                onAnalysChange={setSelectedOption}
                onSelect={handleSelect}
            />
            <div className="main-container">
                <TXTpart
                    content={llContent}
                    onLineClick={handleLineClick}
                    handleProcessCode={handleOptimize}
                    compilerFlags={compilerFlags}
                    setCompilerFlags={setCompilerFlags}
                />
                <SVGpart    
                    functions={listOfFunctions}
                    selectedFunction={selectedFunction}
                    setSelectedFunction={setSelectedFunction}
                    llContent={llContent}
                    svgContent={svgContent}
                    onGetRequest={handleGetRequest}
                    selectedOption={selectedOption}
                    infoContent={loopInfo}
                    onBlockClick={handleBlockClick}
                />
            </div>
        </div>
    );
}

export default App;
