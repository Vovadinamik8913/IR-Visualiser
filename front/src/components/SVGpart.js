import React, {useState, useRef, useEffect} from 'react';
import Snap from 'snapsvg-cjs';
import './styles/SVG.css';

let clickCounter = 0;

const SVGpart = ({
    svgContent,
    functions,
    selectedFunction,
    setSelectedFunction,
    llContent, 
    onGetRequest, 
    selectedOption,
    infoContent,
    onBlockClick,
    howManyClicks,
    listOfLoopBlocks,
    onGetLoopsInfo
}) => {

    const [blockTitle, setBlockTitle] = useState('');
    const [blockNumber, setBlockNumber] = useState('');
    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const [scale, setScale] = useState(1);
    const svgContainerRef = useRef(null);
    const viewTransform = useRef({
        scale: 1,
        translateX: 0,
        translateY: 0
    });
    const lastHighlighted = useRef(null);
    
    const handleSvgClick = (event) => {
        const node = event.target.closest('.node');
        const edge = event.target.closest('.edge');

        if (node) {
            const title = node.querySelector('title')?.textContent || 'Без названия';
            if(blockTitle === title) {
                clickCounter = clickCounter + 1 > 3 ? 1 : clickCounter + 1;
                console.log(clickCounter);
            } else {
                clickCounter = 1;
                console.log(clickCounter);
            }
            setBlockTitle(title);
            const textElement = node.querySelector('text');
            const number = textElement?.textContent.trim().replace(':', '') || 'Нет данных';
            setBlockNumber(number);
            console.log("Информация о блоке:", blockTitle); // Выводим информацию о блоке в консоль
            onBlockClick(blockNumber, blockTitle);
            if (selectedOption === "LoopsInfo" || selectedOption === "Scev" || selectedOption === "DomTree") {
                if(event.ctrlKey) {
                    setPopupVisible(true);
                    setPopupPosition({ x: event.clientX, y: event.clientY });
                }
                else { 
                    setPopupVisible(false);
                }
                howManyClicks(clickCounter);
            }

            const s = Snap(svgContainerRef.current.querySelector('svg'));
            const snapNode = Snap(node);
            const polygon = snapNode.select('polygon');

            if(selectedOption !== 'LoopsInfo') {
                if (lastHighlighted.current) {
                    lastHighlighted.current.attr({
                        fill: '#b70d28',
                        stroke: '#b70d28',
                        'stroke-width': 1,
                        'fill-opacity': 0.44
                    });
                }

                if (polygon && clickCounter===2) {
                    polygon.attr({
                        fill: 'rgba(241,45,80,0.66)',
                        stroke: '#000',
                        'stroke-width': 2,
                        'fill-opacity': 1
                    });

                    lastHighlighted.current = polygon;
                }
            }
        } else if (edge) {
            const blockFrom = edge.querySelector('title')?.textContent.split("->")[0] || 'NO NAME';
            console.log("Ребро из блока:", blockFrom);
            const blockTo = edge.querySelector('title')?.textContent.split('->')[1] || 'NO NAME';
            console.log("в блок:", blockTo);
            setPopupVisible(false);
        } else {
            console.log("Клик вне блоков и рёбер");
            setPopupVisible(false);
        }
    };

    const handleDropdownChange = (event) => {
        setSelectedFunction(event.target.value);
        onGetRequest(event.target.value);
        onGetLoopsInfo(event.target.value);
    };

    useEffect(() => {
        const container = svgContainerRef.current;
        if (!container) return;
        const svgEl = container.querySelector('svg');
        if (!svgEl) return;

        const s = Snap(svgEl);

        let g = s.select('g#zoom-layer');
        if (!g) {
            g = s.group().attr({ id: 'zoom-layer' });

            s.append(g);

            const children = Array.from(svgEl.childNodes);
            children.forEach((child) => {
                if (
                    child.tagName !== 'defs' &&
                    child !== g.node
                ) {
                    g.append(child);
                }
            });
        }

        let isPanning = false;
        let panStart = { x: 0, y: 0 };

        const applyTransform = () => {
            const { scale, translateX, translateY } = viewTransform.current;
            g.transform(`translate(${translateX},${translateY}) scale(${scale})`);
            setPopupVisible(false);
        };

        const handleWheel = (e) => {
            e.preventDefault();
            const delta = e.deltaY > 0 ? -0.01 : 0.01;
            let newScale = viewTransform.current.scale + delta;
            newScale = Math.max(0.1, Math.min(2, newScale));
            viewTransform.current.scale = newScale;
            applyTransform();
        };

        const handleMouseDown = (e) => {
            isPanning = true;
            panStart = { x: e.clientX, y: e.clientY };
        };

        const handleMouseMove = (e) => {
            if (!isPanning) return;
            const dx = e.clientX - panStart.x;
            const dy = e.clientY - panStart.y;
            viewTransform.current.translateX += dx;
            viewTransform.current.translateY += dy;
            panStart = { x: e.clientX, y: e.clientY };
            applyTransform();
        };

        const handleMouseUp = () => {
            isPanning = false;
        };

        svgEl.addEventListener('wheel', handleWheel, { passive: false });
        svgEl.addEventListener('mousedown', handleMouseDown);
        svgEl.addEventListener('mousemove', handleMouseMove);
        window.addEventListener('mouseup', handleMouseUp);

        return () => {
            svgEl.removeEventListener('wheel', handleWheel);
            svgEl.removeEventListener('mousedown', handleMouseDown);
            svgEl.removeEventListener('mousemove', handleMouseMove);
            window.removeEventListener('mouseup', handleMouseUp);
        };
    }, [svgContent]);

    useEffect(() => {
        if (selectedOption === 'LoopsInfo' && listOfLoopBlocks.length > 0) {
            if (!svgContent || !svgContainerRef.current) return;

            const highlightBlocks = listOfLoopBlocks;
            console.log(highlightBlocks);
            const svg = Snap(svgContainerRef.current.querySelector('svg'));

            svg.selectAll('.node').forEach(node => {
                const textEl = node.select('text');
                const blockNumber = textEl?.node.textContent.trim().replace(':', '');

                if (highlightBlocks.includes(parseInt(blockNumber))) {
                    const polygon = node.select('polygon');
                    polygon?.attr({
                        stroke: 'blue',
                        'stroke-width': 4,
                    });
                }
            });
        }
    }, [svgContent, listOfLoopBlocks]);


    return (
        <div className="window">
            { llContent &&
            <div>
                <select
                value={selectedFunction}
                onChange={handleDropdownChange}
                className="dropdown"
                >
                <option value="start">Выберите функцию</option>
                {functions.length > 0 ? (
                    functions.map((func, index) => (
                        <option key={index} value={func}>
                            {func}
                        </option>
                    ))
                ) : (
                    <option disabled>Нет доступных функций</option>
                )}
                </select>
            </div>}
            {svgContent ? (
                <div className="svg-container">
                    <div
                        ref={svgContainerRef}
                        onClick={handleSvgClick}
                        dangerouslySetInnerHTML={{ __html: `
                            <div style='
                            display: inline-block; 
                            align-items: center;
                            justify-content: center;
                            height: fit-content;'>
                              ${svgContent}
                            </div>` }}
                    />
                    {popupVisible && (selectedOption === "LoopsInfo" || selectedOption === "Scev" || selectedOption === "DomTree") && (
                        <div 
                            className="popup-info" 
                            style={{ top: popupPosition.y, left: popupPosition.x }}
                        >
                            {infoContent || "Нет данных"}
                        </div>
                    )}
                </div>
            ) : (
                <div className="svg-placeholder">
                    Здесь будет CFG.
                </div>
            )}
        </div>
    );
};

export default SVGpart;