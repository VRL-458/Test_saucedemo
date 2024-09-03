import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pruebas extends BaseTest {

    @Test
    public void OpccionResetSlideBar() throws InterruptedException {
        WebElement slideBar = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("react-burger-menu-btn")));
        slideBar.click();

        WebElement buttonProduct1 = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("add-to-cart-sauce-labs-backpack")));
        buttonProduct1.click();
        WebElement buttonProduct2 = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("add-to-cart-sauce-labs-bike-light")));
        buttonProduct2.click();
       WebElement slideBarOptionReset = new WebDriverWait(driver, Duration.ofSeconds(5))
               .until(ExpectedConditions.presenceOfElementLocated(By.linkText("Reset App State")));
       slideBarOptionReset.click();
       try {
           WebElement buttonremove1 = new WebDriverWait(driver, Duration.ofSeconds(5))
                   .until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
           WebElement buttonremove2 = new WebDriverWait(driver, Duration.ofSeconds(5))
                   .until(ExpectedConditions.presenceOfElementLocated(By.className("add-to-cart-sauce-labs-bike-light")));

       }
       catch (TimeoutException e) {
           System.out.println("Error el boton de Add to cart no ha vuelto a aparecer ");
       }
        Thread.sleep(5000);
    }

    @Test
    public void VerificarCarritoVacio() throws InterruptedException {
        WebElement cartbutton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
        cartbutton.click();

        WebElement buttonCheckout = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("checkout")));

        Assertions.assertFalse(buttonCheckout.isEnabled()); // se espera que el boton de chekout este desabilitado porque no se tiene productos
    }

    @Test
    public void VerificarPrecioTotal() throws InterruptedException {
        Double precio_total = 0.0;

        WebElement buttonProduct1 = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("add-to-cart-sauce-labs-backpack")));
        buttonProduct1.click();
        WebElement buttonProduct2 = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("add-to-cart-sauce-labs-bike-light")));
        buttonProduct2.click();
        WebElement cartbutton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
        cartbutton.click();

        WebElement buttonCheckout = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("checkout")));
        buttonCheckout.click();
        WebElement textName = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("first-name")));
        textName.sendKeys("Juan");

        WebElement textLastName = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("last-name")));
        textLastName.sendKeys("Garcia");

        WebElement testZip = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("postal-code")));
        testZip.sendKeys("12345");

        WebElement buttonContinue = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("continue")));
        buttonContinue.click();

        List<WebElement> precio =driver.findElements(By.className("inventory_item_price"));
        for(WebElement element : precio){
            String precioProducto = element.getText();
            precioProducto = precioProducto.replace("$", "");
            double precioItem = Double.parseDouble(precioProducto);
            precio_total += precioItem;
        }

        //Precio total calculado en la pagina
        WebElement textPrecio = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("summary_subtotal_label")));
        String textPrecioSummary = textPrecio.getText();
        textPrecioSummary = textPrecioSummary.replace("Item total: $", "");
        double summaryPrecio = Double.parseDouble(textPrecioSummary);
        //que el precio calculado sea igual al precio total del carrito
        Assertions.assertEquals(precio_total, summaryPrecio);

        Thread.sleep(5000);
    }

    @Test
    public void ordenadoPrecio(){

        WebElement sortComboBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));

        Select selectObject =  new Select(sortComboBox);
        selectObject.selectByValue("lohi");

        //verificacion

        List<WebElement> productName = driver.findElements(By.className("inventory-item-price"));

        List<String> actualProductsOrder = new ArrayList<>();
        for(WebElement product : productName){
            actualProductsOrder.add(product.getText());
        }
        boolean isSorted = Ordering.natural().isOrdered(
                actualProductsOrder.stream()
                        .map(s -> Integer.parseInt(s.replace("$", ""))) // Convertir a números
                        .collect(Collectors.toList())
        );


    }

    @Test
    public void revisarProductosAnadidosCarrito()
    {
        List<WebElement> productos = driver.findElements(By.className("inventory_item"));
        List<String> nombresProductosSeleccionados = new ArrayList<>();

        for (WebElement producto : productos) {
            //se gurda el nombre del boton de dicho producto iterado
            WebElement boton = producto.findElement(By.cssSelector(".btn_inventory"));
            String textoBoton = boton.getText();


            if (textoBoton.equalsIgnoreCase("Remove")) {
                WebElement nombreProducto = producto.findElement(By.className("inventory_item_name"));
                nombresProductosSeleccionados.add(nombreProducto.getText());
            }
        }

        //click boton carrito
        WebElement cartbutton = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
        cartbutton.click();

        //Lista de productos en carrito
        List<WebElement> nombreProductosCarrito = driver.findElements(By.className("inventory_item_name"));
        List<String> nombreStringproductos = new ArrayList<>();
        for (WebElement nombreProducto : nombreProductosCarrito)
        {
            nombresProductosSeleccionados.add(nombreProducto.getText());
        }
        Assertions.assertEquals(nombresProductosSeleccionados, nombresProductosSeleccionados);



    }


}
