
const [chartType, setChartType] = useState(1)

const handleTabSelect = (selectedTab) => {
    setChartType(parseInt(selectedTab));
    setFilters();   //is a function that edit an array and the modified array 
                    //trigger an useEffect() that make a rest request and built a chart
}

return(
    <Nav fill variant="tabs" activeKey={chartType} defaultActiveKey="1" onSelect={handleTabSelect}>
        <Nav.Item>
            <Nav.Link eventKey="1">Invoices by Period</Nav.Link>
        </Nav.Item>
        <Nav.Item>
            <Nav.Link eventKey="2">Total by Period</Nav.Link>
        </Nav.Item>
    </Nav>
)
//The problem is that setFilters uses chartType...
//I think that when I process chartType in setFilter() it is not yet modified by setChartType()
