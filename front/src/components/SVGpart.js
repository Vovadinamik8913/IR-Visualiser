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
    listOfLoopBlocks,
    listOfCurLoop,
    onGetLoopsInfo,
    selectedBlock
}) => {
    const [blockTitle, setBlockTitle] = useState('');
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
            clickCounter = (blockTitle === title) ? (clickCounter % 3) + 1 : 1;
            setBlockTitle(title);

            const textElement = node.querySelector('text');
            const number = textElement?.textContent.trim().replace(':', '') || 'Нет данных';
            console.log("Информация о блоке:", blockTitle);

            if (selectedOption === "LoopsInfo" || selectedOption === "Scev") {
                if(event.ctrlKey) {
                    setPopupVisible(true);
                    setPopupPosition({ x: event.clientX, y: event.clientY });
                }
                else { 
                    setPopupVisible(false);
                }
            }
            onBlockClick(number, title, clickCounter);

            const s = Snap(svgContainerRef.current.querySelector('svg'));
            const snapNode = Snap(node);
            const polygon = snapNode.select('polygon');

            if(selectedOption !== 'LoopsInfo') {
                if (lastHighlighted.current) {
                    lastHighlighted.current.attr({
                        stroke: '#000',
                        'stroke-width': 0.1,
                    });
                }

                if (polygon && clickCounter !== 2) {
                    polygon.attr({
                        stroke: '#b222d2',
                        'stroke-width': 5,
                    });

                    lastHighlighted.current = polygon;
                }
            }

        } else if (edge) {
            const [blockFrom, blockTo] = edge.querySelector('title')?.textContent.split('->') || ['NO NAME', 'NO NAME'];
            console.log("Ребро из блока:", blockFrom, "в блок:", blockTo);
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

            Array.from(svgEl.childNodes).forEach(child => {
                if (child.tagName !== 'defs' && child !== g.node) g.append(child);
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
        if (selectedOption === 'LoopsInfo' && listOfCurLoop.length > 0) {
            const svg = Snap(svgContainerRef.current.querySelector('svg'));

            svg.selectAll('.node').forEach(node => {
                const textEl = node.select('text');
                const blockNumber = textEl?.node.textContent.trim().replace(':', '');

                const polygon = node.select('polygon');
                polygon?.attr({
                    stroke: '#b70d28',
                    'stroke-width': 0.44
                })

                if (listOfLoopBlocks.includes(blockNumber)) {
                    const polygon = node.select('polygon');
                    polygon?.attr({
                        stroke: 'blue',
                        'stroke-width': 4,
                    });
                }

                if (listOfCurLoop.includes('%' + blockNumber)) {
                    const polygon = node.select('polygon');
                    polygon?.attr({
                        stroke: 'yellow',
                        'stroke-width': 8,
                    });
                }


            });
        }
    }, [selectedOption, listOfCurLoop]);

    useEffect(() => {
        if (selectedOption === 'LoopsInfo' && listOfLoopBlocks.length > 0) {
            if (!svgContent || !svgContainerRef.current) return;

            const svg = Snap(svgContainerRef.current.querySelector('svg'));

            svg.selectAll('.node').forEach(node => {
                const textEl = node.select('text');
                const blockNumber = textEl?.node.textContent.trim().replace(':', '');

                if (listOfLoopBlocks.includes(blockNumber)) {
                    const polygon = node.select('polygon');
                    polygon?.attr({
                        stroke: 'blue',
                        'stroke-width': 4,
                    });
                }
            });
        }
    }, [selectedOption, svgContent, listOfLoopBlocks]);

    useEffect(() => {
        if (!selectedBlock || !svgContainerRef.current) return;

        const svgEl = svgContainerRef.current.querySelector('svg');
        if (!svgEl) return;

        const s = Snap(svgEl);
        const zoomLayer = s.select('g#zoom-layer');
        if (!zoomLayer) return;

        const allNodes = s.selectAll('.node');
        let targetNode = null;

        allNodes.forEach(node => {
            const text = node.select('text');
            const number = text?.node.textContent.trim().replace(':', '');
            if (number === selectedBlock.toString()) {
                targetNode = node;
            }
        });

        if (targetNode) {
            const bbox = targetNode.getBBox();
            const svgRect = svgEl.getBoundingClientRect();

            const centerX = svgRect.width / 2;
            const centerY = svgRect.height / 2;

            const translateX = centerX - (bbox.x + bbox.width / 2) * viewTransform.current.scale;
            const translateY = centerY - (bbox.y + bbox.height / 2) * viewTransform.current.scale;

            viewTransform.current.translateX = translateX;
            viewTransform.current.translateY = translateY;

            zoomLayer.transform(`translate(${translateX}, ${translateY}) scale(${viewTransform.current.scale})`);
        }
    }, [selectedBlock]);


    return (
        <div className="window">
            { llContent &&
            <div>
                <select
                value={selectedFunction}
                onChange={handleDropdownChange}
                className="dropdown"
                >
                <option value="">Выберите функцию</option>
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
                            justify-content: center;'>
                              ${svgContent}
                            </div>` }}
                    />
                    {popupVisible && (selectedOption === "LoopsInfo" || selectedOption === "Scev") && (
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