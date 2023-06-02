const getCompanyDetails = (company) => [
    {"name": "address", "value": company.address},
    {"name": "Tax Code", "value": company.tax_code},
    {"name": "Postal Code", "value": company.postal_code},
]

export default getCompanyDetails;