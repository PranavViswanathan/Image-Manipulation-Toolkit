## Project Structure

The project is organized into three main packages: `controller`, `model`, and `viewer`, each with a specific role to ensure modularity and separation of concerns. This structure allows for easy maintenance, testing, and future enhancements.

### Controller Package

The `controller` package manages user inputs, command execution, and interactions between the view and model components.

- **CLIView**: Acts as the main entry point for the command-line interface. It initializes the application, accepts commands from the user, and delegates them to appropriate handlers for processing.
  
- **CommandProcessor**: Implements the command pattern to enable a modular, extendable approach to image operations. Each command (like loading, blurring, or saving an image) is encapsulated as an object, allowing complex operations to be queued and executed independently.

- **CompressedImageIO**: Responsible for handling compressed image formats like PNG and JPG. This class provides methods to load and save images in these formats, ensuring compatibility with compressed image standards.

- **UncompressedImageIO**: Specializes in handling PPM (Portable Pixmap) images, which are uncompressed. This class provides methods to read and write PPM files, offering efficient processing for uncompressed data.

- **ScriptProcessor**: Manages the execution of batch scripts that contain sequences of image manipulation commands. It reads each command in a script file, parses it, and processes it in sequence, enabling automated workflows.
- **ImageCache**: A performance optimization class that caches frequently used or recently modified images to speed up processing times, especially in script executions or when applying multiple effects consecutively.


### Model Package

The `model` package contains the core logic and data structures that drive image manipulation, including classes for defining images, pixels, and transformations.

- **Image** and **Pixel**: These classes represent the core data structures for images. `Image` contains a matrix of `Pixel` objects, where each `Pixel` holds RGB color values, allowing fine-grained control over individual pixel manipulation.
  
- **Operations**: This class encapsulates the primary image processing algorithms, including transformations such as blurring, flipping, and brightness adjustments. It serves as a central hub for applying effects and transformations on images.

- **Filtering**: Contains specific implementations of image filters, such as sepia, grayscale, and blur. Each filter is implemented with custom logic tailored for the desired effect, enabling nuanced control over image styling.

- **ImageTransformation**: Handles transformations like flipping (horizontal and vertical) and resizing. This class provides the mathematical operations required for transforming images on the pixel level.


### Viewer Package

The `viewer` package is responsible for managing user interaction, parsing command-line inputs, and interpreting script files.

- **CommandInputHandler**: This component parses and interprets user commands from the command-line interface. It verifies command syntax, ensures that required arguments are provided, and passes commands to `CLIView` for execution.

- **ScriptProcessor**: Although part of the `controller` package, it is integrated into the viewer’s functionality to manage script processing from the user's perspective. It loads, validates, and executes sequences of commands in a script file, providing feedback for each command as it is processed.

Each package contributes to a modular design, making *Imageinator* scalable and maintainable for future updates or additional features. This structure allows for efficient code organization, making it easier for developers to locate and modify specific components of the application.

# Design Patterns in *Imageinator*

## 1. Command Pattern
The **Command Pattern** is central to *Imageinator*'s design, primarily implemented in the `controller` package.
- **Purpose**: It encapsulates requests as objects, allowing you to parameterize clients with different requests, queue them, or log them.
- **Usage in Imageinator**:
  - The `CommandProcessor` class implements this pattern, where each image operation (e.g., loading, saving, blurring) is an individual command object.
  - This pattern provides flexibility, enabling commands to be executed independently, queued, or even undone if needed in future extensions.

## 2. Factory Pattern
The **Factory Pattern** is implicitly used in classes like `CompressedImageIO` and `UncompressedImageIO`, which are responsible for handling different image formats.
- **Purpose**: It provides a way to instantiate objects based on a condition (e.g., image format) without specifying the exact class.
- **Usage in Imageinator**:
  - Depending on the image type (compressed or uncompressed), the appropriate class (`CompressedImageIO` or `UncompressedImageIO`) is instantiated, ensuring that the correct handling logic is applied without changing the code structure.

## 3. Singleton Pattern
The **Singleton Pattern** could be effectively used for managing shared resources, such as the `ImageCache`.
- **Purpose**: Ensures a class has only one instance and provides a global point of access to it.
- **Usage in Imageinator**:
  - The `ImageCache` class can benefit from being a Singleton, as it should ideally manage a single, shared cache across the application, preventing multiple instances from competing for memory resources.

## 4. Strategy Pattern
The **Strategy Pattern** is applied within the `Operations` class and its related filter classes.
- **Purpose**: Allows for defining a family of algorithms, encapsulating each one, and making them interchangeable.
- **Usage in Imageinator**:
  - Each filter (e.g., sepia, grayscale, blur) can be considered a strategy. By implementing a common interface, filters can be applied interchangeably based on user input, making it easier to add new filters without altering existing code.

## 5. Adapter Pattern
The **Adapter Pattern** is utilized for handling different file formats with a consistent interface.
- **Purpose**: It allows incompatible interfaces to work together by providing a "bridge" between them.
- **Usage in Imageinator**:
  - Classes like `CompressedImageIO` and `UncompressedImageIO` act as adapters, enabling the core image manipulation logic to interact seamlessly with different image file formats (e.g., PPM, PNG, JPG).

## 6. Decorator Pattern
The **Decorator Pattern** could be useful in enhancing image functionality, especially when multiple filters need to be applied consecutively.
- **Purpose**: Adds responsibilities to objects dynamically and provides an alternative to subclassing for extending functionality.
- **Usage in Imageinator**:
  - In future enhancements, the `Filtering` and `Operations` classes could leverage decorators to layer multiple filters or transformations, allowing flexible composition of effects.

## 7. Observer Pattern
The **Observer Pattern** might be helpful for live updates, especially if your project integrates real-time changes (like a change in image cache or processing status).
- **Purpose**: Defines a one-to-many dependency where an object (subject) notifies observers of any state changes.
- **Potential Usage in Imageinator**:
  - The `ImageCache` class could notify the system whenever it updates an image, ensuring that other components (e.g., the `CLIView`) are aware of the latest image state.
  - Additionally, `ScriptProcessor` could leverage this pattern to notify the user interface of each command's completion.

## Summary Table

| Pattern         | Purpose                                              | Application in *Imageinator*                          |
|-----------------|------------------------------------------------------|------------------------------------------------------|
| **Command**     | Encapsulates requests as objects for flexibility     | Commands for loading, saving, transforming images    |
| **Factory**     | Instantiates objects based on conditions             | Decides between `CompressedImageIO` and `UncompressedImageIO` based on file type |
| **Singleton**   | Manages shared resources as a single instance        | Potential use for `ImageCache`                       |
| **Strategy**    | Allows interchangeable algorithms                    | Applies different filters dynamically                |
| **Adapter**     | Enables incompatible interfaces to work together     | Handles image formats with a unified interface       |
| **Decorator**   | Adds responsibilities to objects dynamically         | Future enhancement for chaining image effects        |
| **Observer**    | Notifies dependencies of state changes               | Possible use in `ImageCache` or `ScriptProcessor`    |

---




