package gm.rh.controlador;

import gm.rh.excepcion.RecursoNoEncontradoExcepcion;
import gm.rh.modelo.Empleado;
import gm.rh.servicio.IEmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rrhh-app")
@CrossOrigin(value="http://localhost:3000")
public class EmpleadoControlador {
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoControlador.class);
    @Autowired
    private IEmpleadoServicio empleadoServicio;

    @GetMapping("/empleados")
    public List<Empleado> obtenerListaEmpleados(){
        List<Empleado> empleados = this.empleadoServicio.listarEmpleados();
        empleados.forEach(empleado -> logger.info(empleado.toString()));
        return empleados;
    }

    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado){
        logger.info("Empleado a agregar: " + empleado.toString());
        return this.empleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable int id){
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el empleado: " + id);
        }
        return ResponseEntity.ok(empleado);
    }

    @PutMapping("empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable int id, @RequestBody Empleado empleadoRecibido){
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoExcepcion("No se encontró el empleado: " + id);
        }
        empleado.setNombre(empleadoRecibido.getNombre());
        empleado.setDepartamento(empleadoRecibido.getDepartamento());
        empleado.setSueldo(empleadoRecibido.getSueldo());
        this.empleadoServicio.guardarEmpleado(empleado);
        return ResponseEntity.ok(empleado);
    }

    @DeleteMapping("empleados/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarEmpleado(@PathVariable int id){
        Empleado empleado = this.empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoExcepcion("No se encontró el empleado: " + id);
        }
        this.empleadoServicio.eliminarEmpleado(empleado);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", true);
        return ResponseEntity.ok(respuesta);
    }

}
