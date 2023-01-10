// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------DISPLAY PRODUCT-----------------------------------------------------
function displayProduct(product) {
    let content = ``;
    for (let i = 0; i < product.length; i++) {
        content += `<div class="col-xl-3 col-lg-4 col-md-6 col-sm-6 col-xs-6 mb-30px">
             <div class="product">
               <span class="badges">
                <span class="sale">-${product[i].discount}%</span>
                <span class="new">-${product[i].description}%</span>
                   </span>
                 <div class="thumb">
                     <a href="../single-product.html" class="image">
                         <img src="${product[i].image}" alt=""/>
                         <img class="hover-image" src="${product[i].image}" alt=""/>
                     </a>
                 </div>
                 <div class="content">
                     <span class="category"><a href="#">${product[i].category.name}</a></span>
                     <h5 class="title"><a href="../single-product.html">${product[i].name}
                     </a>
                     </h5>
                     <span class="price">
                     <span class="new">$${product[i].price}</span>
                      </span>
                 </div>
                 <div class="actions">
                     <button title="Add To Cart" class="action add-to-cart"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Cart"><i
                         class="pe-7s-shopbag"></i></button>
                     <button class="action wishlist" title="Wishlist"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Wishlist"><i
                         class="pe-7s-like"></i></button>
                     <button class="action quickview" data-link-action="quickview"
                             title="Quick view" data-bs-toggle="modal"
                             data-bs-target="#exampleModal" onclick="displayDetailProduct(${product[i].id})"><i
                         class="pe-7s-look"></i></button>
                     <button class="action compare" title="Compare"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Compare"><i
                         class="pe-7s-refresh-2"></i></button>
                 </div>
             </div>
         </div>`
    }
    content += ``
    document.getElementById('list_product').innerHTML = content;
}

function displayDetailProduct(id) {
    $.ajax({
        headers: {
            Authorization: "Bearer " + sessionStorage.getItem("token"),
        },
        type: "GET",
        url: "http://localhost:8080/" + id,
        success: function (data) {
            $("#nameDetail").val(data.name)
            $("#priceDetail").val(data.price)
            $("#descriptionDetail").val(data.description)
            $("#saleDetail").val(data.discount)
            $("#categoryDetail").val(data.category.id)
            $("#imageUpdate").val(data.image)
            findAllCategory(data)
        }
    });
}

function getAllProduct() {
    $.ajax({
        headers: {
            Authorization: "Bearer " + sessionStorage.getItem("token"),
        },
        type: "GET",
        url: "http://localhost:8080/product",
        success: function (data) {
            displayProduct(data)
        }
    });
}
// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------CREATE PRODUCT------------------------------------------------------
function createProduct() {
    let name = $("#name").val()
    let price = $("#price").val()
    let quantity = $("#quantity").val()
    let description = $("#description").val()
    let discount = $("#discount").val()
    let category = $("#category").val()
    let newProduct = {
        name: name,
        price: price,
        quantity: quantity,
        description: description,
        discount: discount,
        category: {
            id: category
        },
        image: ""
    }

    let formData = new FormData();
    formData.append("file", $("#image")[0].files[0])
    formData.append("product", new Blob([JSON.stringify(newProduct)], {type: 'application/json'}))
    $.ajax({
        headers: {
            // 'Accept': 'application/json',
            // 'Content-Type': 'application/json',
            // Authorization: "Bearer " + sessionStorage.getItem("token"),
        },
        contentType: false,
        processData: false,
        type: "POST",
        url: "http://localhost:8080/uploadProduct",
        data: formData,
        success: function (data) {
            getAllProduct()
            if (data.name != null) {
                Swal.fire(
                    'Good job!',
                    'You clicked the button!',
                    'success'
                )
            }
        }
    })
    event.preventDefault();
}
// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------DISPLAY CATEGORY------------------------------------------------------
function displayCategory(category) {
    return `<option value="${category.id}">${category.name}</option>`
}

function findAllCategory(product) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/category",
        success: function (data) {
            let content = `<select id="category">`
            if (product != null) {
                content += `<option value="${product.category.id}">${product.category.name}</option>`
                for (let i = 0; i < data.length; i++) {
                    if (product.category.id !== data[i].id) {
                        content += displayCategory(data[i])
                    }
                }
                content += `<select>`
                document.getElementById("categoryUpdate").innerHTML = content;
            } else {
                for (let i = 0; i < data.length; i++) {
                    content += displayCategory(data[i])
                }
                content += `<select>`
                document.getElementById("categoryForm").innerHTML = content;
            }
        }
    });
}
// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------DISPLAY PRODUCT ON A STORE------------------------------------------
function displayOneProduct(product) {
    let content = ``;
    for (let i = 0; i < product.length; i++) {
        content += `<div class="col-lg-4 col-md-6 col-sm-6 col-xs-6 mb-30px">
             <div class="product">
               <span class="badges">
                <span class="new" style="color: darkgray">-${product[i].discount}%</span>
                
                   </span>
                 <div class="thumb">
                     <a href="../single-product.html" class="image">
                         <img src="${product[i].image}" alt=""/>
                         <img class="hover-image" src="${product[i].image}" alt=""/>
                     </a>
                 </div>
                 <div class="content">
                     <span class="category"><a href="#">${product[i].category.name}</a></span>
                     <h5 class="title"><a href="../single-product.html">${product[i].name}
                     </a>
                     </h5>
                     <span class="price">
                   
                     <span class="old">$${product[i].price}</span>
                     <span class="new">$${((product[i].price) * (100 - product[i].discount)) / 100}</span>
                      </span>
                 </div>
                 <div class="actions">
                     <button title="Add To Cart" class="action add-to-cart"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Cart"><i
                         class="pe-7s-shopbag"></i></button>
                     <button class="action wishlist" title="Wishlist"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Wishlist"><i
                         class="pe-7s-like"></i></button>
                     <button class="action quickview" data-link-action="quickview"
                             title="Quick view" data-bs-toggle="modal"
                             data-bs-target="#exampleModal"><i
                         class="pe-7s-look"></i></button>
                     <button class="action compare" title="Update" data-bs-target="#exampleModal4" data-bs-whatever="@mdo" data-bs-toggle="modal"
                             onclick="updateForm(${product[i].id})"><i
                         class="pe-7s-refresh-2"></i></button>
                         <button class="action compare" title="Delete"                             
                             onclick="deleteProduct(${product[i].id})">
                             <i class="fa-light fa-trash"></i>
                         </button>
                 </div>
             </div>
         </div>`
    }

    document.getElementById('list_product_store').innerHTML = content;
}

function getOneProduct() {
    let idStore = sessionStorage.getItem("idStore")
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/product/" + idStore,
        success: function (data) {
            displayOneProduct(data)
        }
    });
}

function displayProductShop(id) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/store/" + id,
        success: function (data) {
            transferPage(data)
            sessionStorage.setItem("idStore", id)
        }
    })
}
// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------CREATE A PRODUCT ON STORE------------------------------------------------------
function createOneProduct() {
    let name = $("#name").val()
    let price = $("#price").val()
    let quantity = $("#quantity").val()
    let description = $("#description").val()
    let discount = $("#discount").val()
    let category = $("#category").val()
    let store = $("#store").val()
    let newProduct = {
        name: name,
        price: price,
        quantity: quantity,
        description: description,
        discount: discount,
        category: {
            id: category
        },
        store: {
            id: store
        },
        image: ""
    }

    let formData = new FormData();
    formData.append("file", $("#image")[0].files[0])
    formData.append("product", new Blob([JSON.stringify(newProduct)], {type: 'application/json'}))
    $.ajax({
        headers: {
            // 'Accept': 'application/json',
            // 'Content-Type': 'application/json',
            // Authorization: "Bearer " + sessionStorage.getItem("token"),
        },
        contentType: false,
        processData: false,
        type: "POST",
        url: "http://localhost:8080/uploadProduct",
        data: formData,
        success: function (data) {
            displayProductShop(data)
            getOneProduct(data)
        }
    })
    event.preventDefault();
}

// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------UPDATE PRODUCT ON STORE------------------------------------------------------
function updateForm(id) {
    sessionStorage.setItem("update", id)
    $.ajax({
        headers: {
            Authorization: "Bearer " + sessionStorage.getItem("token"),
        },
        type: "GET",
        url: "http://localhost:8080/" + id,
        success: function (data) {
            $("#nameUpdate").val(data.name)
            $("#priceUpdate").val(data.price)
            $("#quantityUpdate").val(data.quantity)
            $("#descriptionUpdate").val(data.description)
            $("#discountUpdate").val(data.discount)
            $("#categoryUpdate").val(data.category.id)
            $("#storeUpdate").val(data.store.id).val(data.store.nameStore)
            sessionStorage.setItem("IdStore", data.store.id)
            sessionStorage.setItem("NameStore", data.store.nameStore)
            findAllCategory(data)
            findAllStore(data)
        }
    });
}

function updateProduct() {
    let idUpdate = sessionStorage.getItem("update");
    let name = $("#nameUpdate").val()
    let price = $("#priceUpdate").val()
    let quantity = $("#quantityUpdate").val()
    let description = $("#descriptionUpdate").val()
    let discount = $("#discountUpdate").val()
    let categoryId = $("#category").val()
    let storeId = $("#store").val()
    let ProductUpdate = {
        id: idUpdate,
        name: name,
        price: price,
        quantity: quantity,
        description: description,
        discount: discount,
        image: "",
        category: {
            id: categoryId
        },
        store: {
            id: storeId
        }
    }
    let formData = new FormData()
    formData.append("fileUpdate", $("#imageUpdate")[0].files[0])
    formData.append("product", new Blob([JSON.stringify(ProductUpdate)],
        {type: 'application/json'}))
    $.ajax({
        contentType: false,
        processData: false,
        type: "PUT",
        url: "http://localhost:8080/" + idUpdate,
        data: formData,
        success: function (data) {
            getOneProduct(data)
        }
    });
    event.preventDefault();
}

function findAllStore() {
    let IdStore = sessionStorage.getItem("IdStore")
    let NameStore = sessionStorage.getItem("NameStore")
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/store",
        success: function (data) {
            let content = `<select id="store">`
            if (IdStore != null) {
                content += `<option value="${IdStore}">${NameStore}</option>`
                for (let i = 0; i < data.length; i++) {
                    if (IdStore !== data[i].id) {
                        content += displayStore(data[i])
                    }
                }
                content += `<select>`
                document.getElementById("storeUpdate").innerHTML = content;
            } else {
                for (let i = 0; i < data.length; i++) {
                    content += displayStore(data[i])
                }
                content += `<select>`
                document.getElementById("storeForm").innerHTML = content;
            }
        }
    });
}

function displayStore(store) {
    return `<option value="${store.id}">${store.nameStore}</option>`
}
// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------DELETE PRODUCT------------------------------------------------------
function deleteProduct(id) {
    if (confirm("ARE YOU SURE WANNA DELETE THIS PRODUCT?")) {
        $.ajax({
            headers: {
                Authorization: "Bearer " + sessionStorage.getItem("token"),
            },
            type: "DELETE",
            url: "http://localhost:8080/" + id,
            success: function (data) {
                getOneProduct()
                if (data !== "") {
                    alert("Delete successfully!")
                }
            }
        });
    }
}

// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------SEARCH PRODUCT------------------------------------------------------
function searchProduct(page) {
    let text = $("#search").val()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/searchProduct?search=" + text + "&page=" + page + "&size=8",
        success: function (data) {
            displayProductPage(data.content)
            displayPage(data)
            //điều kiện bỏ nút previous
            if (data.pageable.pageNumber === 0) {
                document.getElementById("backup").hidden = true
            }
            //điều kiện bỏ nút next
            if (data.pageable.pageNumber + 1 === data.totalPages) {
                document.getElementById("next").hidden = true
            }
        }
    })
    event.preventDefault()
}

function getProduct(product) {
    return `<div class="col-xl-3 col-lg-4 col-md-6 col-sm-6 col-xs-6 mb-30px">
             <div class="product">
               <span class="badges">
                <span class="sale">-${product.discount}%</span>
                   </span>
                 <div class="thumb">
                     <a href="../single-product.html" class="image">
                         <img src="${product.image}" alt=""/>
                         <img class="hover-image" src="${product.image}" alt=""/>
                     </a>
                 </div>
                 <div class="content">
                     <span class="category"><a href="#">${product.category.name}</a></span>
                     <h5 class="title"><a href="../single-product.html">${product.name}
                     </a>
                     </h5>
                     <span class="price">
                     <span class="old">$${product.price}</span>
                     <span class="new">$${((product.price) * (100 - product.discount)) / 100}</span>
                    
                      </span>
                 </div>
                 <div class="actions">
                     <button title="Add To Cart" class="action add-to-cart"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Cart"><i
                         class="pe-7s-shopbag"></i></button>
                     <button class="action wishlist" title="Wishlist"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Wishlist"><i
                         class="pe-7s-like"></i></button>
                     <button class="action quickview" data-link-action="quickview"
                             title="Quick view" data-bs-toggle="modal"
                             data-bs-target="#exampleModal" onclick="displayDetailProduct(${product.id})"><i
                         class="pe-7s-look"></i></button>
                     <button class="action compare" title="Compare"
                             data-bs-toggle="modal"
                             data-bs-target="#exampleModal-Compare"><i
                         class="pe-7s-refresh-2"></i></button>
                 </div>
             </div>
         </div>`
}

function displayProductPage(data) {
    let content = ``;
    for (let i = 0; i < data.length; i++) {
        content += getProduct(data[i])
    }
    document.getElementById('list_product').innerHTML = content;
}

function displayPage(data) {
    document.getElementById('pageForm').innerHTML = `<ul>
                                        <li class="li"><a class="page-link" id="backup" onclick="isPrevious(${data.pageable.pageNumber})"><i class="fa fa-angle-left"></i></a>
                                        </li>
                                        <li class="li"><span>${data.pageable.pageNumber + 1} | ${data.totalPages}</span></a>
                                        </li>
                                        <li class="li"><a class="page-link" id="next" onclick="isNext(${data.pageable.pageNumber})"><i class="fa fa-angle-right"></i></a>
                                        </li>
                                    </ul>`
}

function isPrevious(pageNumber) {
    searchProduct(pageNumber - 1)
}

//hàm tiến page
function isNext(pageNumber) {
    searchProduct(pageNumber + 1)
}

// ---------------------------------------------o0o-----o0o--------------------------------------------------------
//---------------------------------------------SORT PRODUCT------------------------------------------------------
function sortProductByPrice() {
    let text1 = $("#sortPrice").val()
    let text = (text1.split(","))
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/sortProductByPrice?price1=" + text[0] + "&price2=" + text[1],
        success: function (data) {
            displayProductStore(data.content)
        }
    })
}

function sortProductByCategory() {
    let text = $("#sortCategory").val()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/sortProductByCategory?price1=" + text[0] + "&price2=" + text[1],
        success: function (data) {
            displayProductStore(data.content)
        }
    })
}







