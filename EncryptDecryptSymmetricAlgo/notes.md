Symmetric Algorithm: Symmetric cryptography, also known as secret-key or shared-key encryption, uses a single key for both encryption and decryption. This means that the sender and receiver must share a common key securely. The primary challenge is key distribution – both parties need a way to exchange the key in a secure manner. Examples include AES and Blowfish.

Asymmetric Algorithm: Asymmetric cryptography, or public-key cryptography, uses two different keys: one for encryption and another for decryption. These keys are known as the public key and the private key. The public key, as the name suggests, can be shared with anyone, while the private key is kept secret. Data that is encrypted with the public key can only be decrypted by the private key and vice versa. This method addresses the key distribution problem found in symmetric algorithms. RSA and Elliptic Curve are examples of asymmetric algorithms.

## KeyStore 

B1) What is the KeyStore class? Differentiate the use of it to saving unencrypted keys directly to file.

KeyStore is a class in the Java security API that provides access to a secure storage facility for cryptographic keys and certificates. Here's a differentiation:

Using KeyStore:

Security: KeyStore provides a mechanism to securely store cryptographic keys in an encrypted format. It often requires a password to access the stored keys.
Organization: It can hold multiple keys and certificates, making it suitable for applications that need to manage multiple cryptographic entities.
Formats: There are different types of KeyStores like JKS, PKCS12, etc.
Saving keys directly to a file:

Security: If you save a key unencrypted directly to a file, it's exposed. Anyone with access to the file can read and use the key.
Simplicity: It's straightforward. You write the key bytes to a file and read them when needed.
Risks: In a real-world scenario, this approach is risky. If the file gets compromised, the key is immediately exposed.

KeyStore Storage Location and OS Involvement:

The KeyStore is essentially a file, often with extensions like .jks (for Java KeyStore) or .p12 (for PKCS12 format). Its location depends on where you or the application chooses to save it.
The operating system isn't directly involved in the cryptographic operations of the KeyStore, but it plays a role in file system-level security. For example, on Linux/Unix systems, you might set restrictive permissions on the KeyStore file to enhance security.
The concept of a KeyStore is consistent across platforms, but how you protect and manage that file might vary depending on file system and OS features.

Suitable Place to Store the KeyStore File:

Private Directories: If you're developing a standalone application, it's common to store the KeyStore in a private directory that only the application (and potentially the user running the application) can access.

Configurable Path: For flexibility, many applications allow the administrator or user to specify the path to the KeyStore via configuration. This way, users can decide the most secure location based on their infrastructure and security policies.

System/Environment Properties: Another approach is to specify the KeyStore location using system properties or environment variables.

Embedded Systems: For certain embedded or constrained environments, the KeyStore might reside in a specific hardware-protected location or use hardware-based security modules (HSM).

Always Backup: Regardless of location, ensure that the KeyStore is regularly backed up. If lost, encrypted data might become irretrievable.

## Use PKCS12 over JKS
Recommendation: If you're starting a new project or don't have a specific reason to use JKS, go with PKCS12. It's more secure and has the benefit of being an industry-standard format, making it easier to work with tools outside the Java ecosystem.