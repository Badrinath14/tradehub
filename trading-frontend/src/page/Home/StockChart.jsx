import React, { useState } from "react";
import ReactApexChart from "react-apexcharts";
import { Button } from "@/components/ui/button";

const StockChart = () => {
  const [activeLabel, setActiveLabel] = useState("1 Day");

  const timeseries = [
    {
      keyword: "DIGITAL_CURRENCY_DAILY",
      key: "Time Series (Daily)",
      label: "1 Day",
      value: 1,
    },
    {
      keyword: "DIGITAL_CURRENCY_WEEKLY",
      key: "Weekly Time Series",
      label: "1 Week",
      value: 7,
    },
    {
      keyword: "DIGITAL_CURRENCY_MONTHLY",
      key: "Monthly Time Series",
      label: "1 Month",
      value: 30,
    },
  ];

  const series = [
    {
      data: [
        [1717322619938, 67489.1712726232],
        [1717326109959, 67485.44310797089],
        [1717329785067, 68209.41827518264],
        [1717333467723, 67873.0403342155],
        [1717337090959, 68103.29503896789],
        [1717340632358, 68252.69063146935],
        [1717344056445, 68087.46074981014],
        [1717347965451, 68048.36591430628],
        [1717351643523, 67635.973112701],
        [1717354892842, 67668.5505292233],
        [1717358699127, 67723.23906127528],
        [1717362150595, 67792.33246132988],
        [1717365750996, 67789.29441510144],
        [1717369629904, 67958.52076774689],
        [1717373002962, 67749.92965318475],
        [1717376636890, 67783.2402349144],
        [1717380430288, 68667.7634095987],
        [1717383973327, 68405.56828707094],
        [1717387668627, 68283.9818557212],
        [1717390859391, 68516.1647819721],
        [1717394541940, 68577.855546556],
        [1717398137611, 69006.79789734057],
        [1717401843301, 68842.69737622877],
        [1717405672746, 69060.83559838489],
        [1717408956769, 69011.34276995264],
        [1717412492687, 68901.81994972998],
        [1717416268021, 69075.41221249818],
        [1717419709466, 69659.9125720908],
        [1717423727975, 69657.23129521386],
      ],
    },
  ];

  const options = {
    chart: {
      id: "area-datetime",
      type: "area",
      height: 350,
      zoom: {
        autoScaleYaxis: true,
      },
    },
    dataLabels: {
      enabled: false,
    },
    xaxis: {
      type: "datetime",
      tickAmount: 6,
    },
    colors: ["#758AA2"],
    markers: {
      colors: ["#fff"],
      strokeColor: "#fff",
      size: 0,
      strokeWidth: 1,
      style: "hollow",
    },
    tooltip: {
      theme: "dark",
    },
    fill: {
      type: "gradient",
      gradient: {
        shadeIntensity: 1,
        opacityFrom: 0.7,
        opacityTo: 0.9,
        stops: [0, 100],
      },
    },
    grid: {
      borderColor: "#47535E",
      strokeDashArray: 4,
      show: true,
    },
  };

  const handleActiveLabel = (value) => {
    setActiveLabel(value);
  };

  return (
    <div>
      <div className="space-x-3">
        {timeseries.map((item) => (
          <Button
            variant={activeLabel === item.label ? "" : "outline"}
            onClick={() => handleActiveLabel(item.label)}
            key={item.label}
          >
            {item.label}
          </Button>
        ))}
      </div>
      <div id="chart-timelines">
        <ReactApexChart
          options={options}
          series={series}
          height={450}
          type="area"
        />
      </div>
    </div>
  );
};

export default StockChart;
