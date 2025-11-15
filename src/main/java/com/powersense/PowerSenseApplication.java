package com.powersense;

// 🔹 Solo mientras pruebas sin base de datos
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.*;
import com.powersense.inventory.devices.infrastructure.persistence.inmemory.InMemoryDeviceRepository;
import com.powersense.inventory.devices.infrastructure.persistence.inmemory.InMemoryRoomRepository;

// 🔹 Imports base de Spring Boot
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


/**
 * ⚙️ Clase principal de PowerSense.
 * Modo sin base de datos (datos falsos en memoria)
 *
 * 👉 Cuando conectes una BD real (PostgreSQL, MySQL, etc.):
 *  1. Elimina las líneas relacionadas con InMemoryDeviceRepository y InMemoryRoomRepository.
 *  2. Quita el bloque @EventListener para que no se carguen datos falsos.
 *  3. Habilita JPA nuevamente en application.properties.
 */
@SpringBootApplication(
        exclude = {org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class}
)
public class PowerSenseApplication {
    public static void main(String[] args) {
        SpringApplication.run(PowerSenseApplication.class, args);
    }

    // ✅ Bean temporal para repositorio en memoria
    @Bean
    public InMemoryDeviceRepository deviceRepository() {
        return new InMemoryDeviceRepository();
    }

    // ✅ Bean temporal para habitaciones (rooms)
    @Bean
    public InMemoryRoomRepository roomRepository() {
        return new InMemoryRoomRepository();
    }

    /**
     * ✅ Carga de datos falsos al iniciar la app.
     * (Solo para pruebas locales sin base de datos)
     */
    @Bean
    public CommandLineRunner loadFakeData() {
        return args -> {
        InMemoryDeviceRepository deviceRepo = deviceRepository();

        // 🔹 Crear ubicaciones simuladas (rooms)
        Location livingRoom = new Location(
                new RoomId("room-1"),
                new RoomName("Sala principal")
        );
        Location kitchen = new Location(
                new RoomId("room-2"),
                new RoomName("Cocina")
        );

        // 🔹 Crear especificaciones de energía simuladas
        PowerSpecification lightPower = new PowerSpecification(60, 220, 1);
        PowerSpecification tvPower = new PowerSpecification(150, 220, 2);

        // 🔹 Crear dispositivos con todos sus value objects
        Device device1 = Device.create(
                new DeviceId("dev-1"),
                new DeviceName("Lámpara LED"),
                DeviceCategory.LIGHT,
                livingRoom,
                lightPower
        );

        Device device2 = Device.create(
                new DeviceId("dev-2"),
                new DeviceName("Televisor Samsung"),
                DeviceCategory.TV,
                livingRoom,
                tvPower
        );

        Device device3 = Device.create(
                new DeviceId("dev-3"),
                new DeviceName("Refrigeradora LG"),
                DeviceCategory.REFRIGERATOR,
                kitchen,
                new PowerSpecification(200, 220, 3)
        );

        // 🔹 Guardar en memoria
        deviceRepo.saveAll(List.of(device1, device2, device3));

        System.out.println("✅ Datos falsos cargados correctamente (modo sin BD)");
        };
    }
}



