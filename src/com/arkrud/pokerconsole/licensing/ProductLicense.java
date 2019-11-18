/**
 * This is a sample class to save and load license information from disk. For
 * simplicity of this demonstration application, it also includes all validation
 * methods.
 *
 */
package com.arkrud.pokerconsole.licensing;

import com.license4j.ActivationStatus;
import com.license4j.DefaultFloatingLicenseInvalidHandlerImpl;
import com.license4j.DefaultFloatingLicenseServerConnectionErrorHandlerImpl;
import com.license4j.DefaultOnlineLicenseKeyCheckTimerHandlerImpl;
import com.license4j.License;
import com.license4j.LicenseValidator;
import com.license4j.ModificationStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

public class ProductLicense {

    // public key
    // TODO: CHANGE THIS PUBLIC KEY WITH YOUR PRODUCTS PUBLIC KEY
    /*public static String publicKey =
            "30819f300d06092a864886f70d010101050003818d003081893032301006072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e000446785c6e3e8ba9ceac781ac490942906ccd2aeb91952e6b67c81628eG02818100835c30414748eaa33518ded88949b37fe812ae5c65592d95491240f3a4c08d6a4210ee2a4806cc676cd42904c35265bfbaef604e44b03a88b119c924eb579374c72761843e01477a409bcea231a6fd1ee3cdc935d37572f3e0d41bf8a6553dbc03RSA4102413SHA512withRSAebd59be3c6141c8a5aad47948322243d5606e0a22cbaa40b99cf5d08e3ccca110203010001";
    */
	public static String publicKey = 
			"30819f300d06092a864886f70d010101050003818d003081893032301006072a8648ce3d02002EC311215SHA512withECDSA106052b81040006031e00040c42f7bca35c1430554093b21a48aed2f1c7ddb36a924efa93409536G02818100b5ae2e8ef8f19c7ac59ac2f4df95f938ae693f161354e20f6c8df7f9d442b3c89d8785d23083973c0274d6b83b21672671fc13b88f2fca1c1c8a562b65beb7fdb591cf374d59afd93a6b28131ab36ddae80bdb360ff984d0bc83c3d63f38b02c03RSA4102413SHA512withRSAf9b4c5f73a3155bfd26ea6396264b1822d53ce6cf4767717553a4e1b530828ef0203010001";
    // license type
    // 1 = license text
    // 2 = floating license text
    // 3 = license key
    // 4 = online license key
    private int licenseType = 3;
    // license string
    private String licenseString;
    // activated license type
    private int activatedLicenseType = 0;
    // activated license string
    private String activatedLicenseString;
    // floating license server hostname
    private String hostname;
    // floating license server port number
    private int portnumber = 0;
    // TODO: DEFINE YOUR AUTO LICENSE GENERATION AND ACTIVATION SERVER ADDRESS
    // TO USE ONLINE.LICENSE4J SET THIS TO null.
    private String activationServerAddr = null;
    // license deactivation server
    // TODO: DEFINE YOUR AUTO LICENSE GENERATION AND ACTIVATION SERVER ADDRESS
    // TO USE ONLINE.LICENSE4J SET THIS TO null.
    private String deactivationServerAddr = null;
    // online license key validation server
    // TODO: DEFINE YOUR AUTO LICENSE GENERATION AND ACTIVATION SERVER ADDRESS
    // TO USE ONLINE.LICENSE4J SET THIS TO null.
    private String validationServerAddr = null;
    // license modification server address
    // TODO: DEFINE YOUR AUTO LICENSE GENERATION AND ACTIVATION SERVER ADDRESS
    // TO USE ONLINE.LICENSE4J SET THIS TO null.
    private String modificationServerAddr = null;
    // Properties to keep everything and save/load from/to disk
    private Properties properties;
    // config file path
    // TODO: WHERE DO YOU WANT TO SAVE LICENSE, DEFINE HERE.
    private String configFile = System.getProperty("user.home") + File.separator + "sample-app.conf";

    public ProductLicense() {
        properties = new Properties();
    }

    public License validateLicense(final boolean beforeActivation) {
        // before activation is here because after activation, activated license
        // must be validated. BUT before activation, always not activated license
        // must be validated.
        License license = null;

        final ProgressDialog progress = new ProgressDialog(null, true);

        // All license validation methods run in swingworker, you can use
        // threads in a better way.
        SwingWorker worker = new SwingWorker() {
            License license = null;

            @Override
            protected void done() {
                progress.setVisible(false);
            }

            @Override
            protected License doInBackground() {
                String licenseStringToValidate;
                int licenseStringToValidateType;
                if (activatedLicenseString != null && !beforeActivation) {
                    // !beforeActivation, so validate the latest activated license string.
                    licenseStringToValidate = activatedLicenseString;
                    licenseStringToValidateType = activatedLicenseType;
                } else {
                    // beforeActivation, it will be activate, so validate the original not activated license string.
                    licenseStringToValidate = licenseString;
                    licenseStringToValidateType = licenseType;
                }

                switch (licenseStringToValidateType) {
                    case 1: // license text
                        license = LicenseValidator.validate(
                                licenseStringToValidate, // REQUIRED - license string
                                publicKey, // REQUIRED - public key
                                // TODO: REPLACE WITH YOUR PRODUCT ID
                                "smart-console", // REQUIRED - product id
                                // TODO: REPLACE WITH YOUR PRODUCT EDITION
                                null, // product edition if needed
                                // TODO: REPLACE WITH YOUR PRODUCT VERSION
                                null, // product version if needed
                                null, // current date, null for current date
                                null); // product release date if needed

                        return license;
                    case 2: // floating license text
                        InetAddress host;
                        try {
                            host = InetAddress.getByName(hostname);

                            license = LicenseValidator.validate(
                                    publicKey, // REQUIRED - public key
                                    // TODO: REPLACE WITH YOUR PRODUCT ID
                                    "example-application", // REQUIRED - product id
                                    // TODO: REPLACE WITH YOUR PRODUCT EDITION
                                    null, // product edition if needed
                                    // TODO: REPLACE WITH YOUR PRODUCT VERSION
                                    null, // product version if needed
                                    null, // current date, null for current date
                                    null, // product release date if needed
                                    host, // REQUIRED - license server host
                                    portnumber, // REQUIRED - license server port number
                                    null, // floating license valid handler
                                    // TODO here you can use custom handlers, we used simple default handler.
                                    new DefaultFloatingLicenseInvalidHandlerImpl("License Invalid, System.exit will be called.", true), // floating license invalid handler
                                    new DefaultFloatingLicenseServerConnectionErrorHandlerImpl("Server Connection Error, System.exit will be called.", true)); // floating license server connection error handler
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        return license;
                    case 3: // license key
                        license = LicenseValidator.validate(
                                licenseStringToValidate, // REQUIRED - license key
                                publicKey, // REQUIRED - public key
                                // TODO: REPLACE WITH YOUR INTERNAL HIDDEN STRING
                                "poker-app", // REQUIRED IF BASIC LICENSE KEY - Internal hidden string
                                null, // Customer name for validation
                                null, // Customer's company name for validation
                                0); // Hardware ID selection

                        return license;
                    case 4: // online license key
                        license = LicenseValidator.validate(
                                licenseStringToValidate, // REQUIRED - license key
                                publicKey, // REQUIRED - public key
                                // TODO: REPLACE WITH YOUR PRODUCT ID
                                "example-application", // REQUIRED - product id
                                // TODO: REPLACE WITH YOUR PRODUCT EDITION
                                null, // product edition if needed
                                // TODO: REPLACE WITH YOUR PRODUCT VERSION
                                null, // product version if needed
                                null, // current date
                                null, // current product version release date
                                validationServerAddr, // validation server address
                                // TODO here you can use custom handlers, we used simple default handler.
                                new DefaultOnlineLicenseKeyCheckTimerHandlerImpl("Online Key Validation Error", false)); // invalid key handler timer

                        return license;
                    default:
                        return null;
                }
            }
        };

        worker.execute();
        progress.setVisible(true);

        try {
            license = (License) worker.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return license;
    }

    public License activateLicense() {
        License license = null;

        final ProgressDialog progress = new ProgressDialog(null, true);

        SwingWorker worker = new SwingWorker() {
            License activatedLicense;

            @Override
            protected void done() {
                if (activatedLicense.getActivationStatus() == ActivationStatus.ACTIVATION_COMPLETED) {
                    // activation completed, set activated license string.
                    setActivatedLicenseString(activatedLicense.getLicenseString());

                    // set activated license type (license text or activatition code)
                    if (activatedLicense.getLicenseText() != null) {
                        // license text
                        setActivatedLicenseType(1);
                    } else {
                        // activation code
                        setActivatedLicenseType(3);
                    }

                    // save license in config file
                    saveLicense();
                }

                progress.setVisible(false);
            }

            @Override
            protected License doInBackground() {
                // To get the not activated license object, call validate method.
                License lic = validateLicense(true);
                // HERE you should check whether license is valid or not, 
                // if it is valid, then activate it.
                activatedLicense = LicenseValidator.autoActivate(lic, activationServerAddr);

                return activatedLicense;
            }
        };

        worker.execute();
        progress.setVisible(true);

        try {
            license = (License) worker.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return license;
    }

    public License deactivateLicense() {
        License license = null;

        final ProgressDialog progress = new ProgressDialog(null, true);

        SwingWorker worker = new SwingWorker() {
            License deactivatedLicense;

            @Override
            protected void done() {
                if (deactivatedLicense.getActivationStatus() == ActivationStatus.DEACTIVATION_COMPLETED) {
                    // If successfull deactivated, clear activated license string and type.
                    setActivatedLicenseString(null);
                    setActivatedLicenseType(0);

                    // save
                    saveLicense();
                }

                progress.setVisible(false);
            }

            @Override
            protected License doInBackground() {
                // To get the License object, validate it.
                License license = validateLicense(false);
                // deactivate, IF IT IS VALID ONLY, so check its validation status here.
                deactivatedLicense = LicenseValidator.autoDeactivate(license, deactivationServerAddr);

                return deactivatedLicense;
            }
        };

        worker.execute();
        progress.setVisible(true);

        try {
            license = (License) worker.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return license;
    }

    public License modifyLicense(final String modificationKey) {
        License license = null;

        final ProgressDialog progress = new ProgressDialog(null, true);

        SwingWorker worker = new SwingWorker() {
            License modifiedLicense;

            @Override
            protected void done() {
                if (modifiedLicense.getModificationStatus() == ModificationStatus.MODIFICATION_COMPLETED
                        || modifiedLicense.getModificationStatus() == ModificationStatus.MODIFICATION_COMPLETED_PREVIOUSLY) {
                    // LICENSE IS MODIFIED, SO SET THE NEW ACTIVATED LICENSE STRING AND SAVE
                    setActivatedLicenseString(modifiedLicense.getLicenseString());
                    setActivatedLicenseType(1);

                    // save
                    saveLicense();
                }

                progress.setVisible(false);
            }

            @Override
            protected License doInBackground() {
                // Again, to get the License object validate it.
                License lic = validateLicense(false);
                // Modify, if it is valid.
                modifiedLicense = LicenseValidator.modifyLicense(lic, modificationServerAddr, modificationKey);

                return modifiedLicense;
            }
        };

        worker.execute();
        progress.setVisible(true);

        try {
            license = (License) worker.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return license;
    }

    /**
     * Save license information, and server information to config file.
     *
     */
    public boolean saveLicense() {
        try {
            if (licenseString != null) {
                properties.setProperty("license-string", licenseString);
            } else {
                properties.remove("license-string");
            }

            if (activatedLicenseString != null) {
                properties.setProperty("activated-license-string", activatedLicenseString);
            } else {
                properties.remove("activated-license-string");
            }

            if (licenseType != 0) {
                properties.setProperty("license-type", String.valueOf(licenseType));
            }

            if (activatedLicenseType != 0) {
                properties.setProperty("activated-license-type", String.valueOf(activatedLicenseType));
            }

            if (hostname != null) {
                properties.setProperty("floating-license-server-hostname", hostname);
            } else {
                properties.remove("floating-license-server-hostname");
            }

            if (portnumber != 0) {
                properties.setProperty("floating-license-server-port", String.valueOf(portnumber));
            }

            properties.store(new FileOutputStream(configFile, false), null);

            return true;
        } catch (IOException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Load license information, and server information from config file.
     *
     */
    public boolean loadLicense() {
        try {
            properties.load(new FileInputStream(configFile));

            licenseString = properties.getProperty("license-string");
            activatedLicenseString = properties.getProperty("activated-license-string");
            if (properties.getProperty("license-type") != null) {
                licenseType = Integer.parseInt(properties.getProperty("license-type"));
            }
            if (properties.getProperty("activated-license-type") != null) {
                activatedLicenseType = Integer.parseInt(properties.getProperty("activated-license-type"));
            }

            hostname = properties.getProperty("floating-license-server-hostname");
            if (properties.getProperty("floating-license-server-port") != null) {
                portnumber = Integer.parseInt(properties.getProperty("floating-license-server-port"));
            }

            return true;
        } catch (IOException ex) {
            Logger.getLogger(ProductLicense.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    // Getter and Setter methods below.
    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseString() {
        return licenseString;
    }

    public void setLicenseString(String licenseString) {
        this.licenseString = licenseString;

        // clear old activated license if available.
        setActivatedLicenseType(0);
        setActivatedLicenseString(null);
    }

    public String getActivatedLicenseString() {
        return activatedLicenseString;
    }

    public void setActivatedLicenseString(String activatedLicenseString) {
        this.activatedLicenseString = activatedLicenseString;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPortnumber() {
        return portnumber;
    }

    public void setPortnumber(int portnumber) {
        this.portnumber = portnumber;
    }

    public String getModificationServerAddr() {
        return modificationServerAddr;
    }

    public void setModificationServerAddr(String modificationServerAddr) {
        this.modificationServerAddr = modificationServerAddr;
    }

    public int getActivatedLicenseType() {
        return activatedLicenseType;
    }

    public void setActivatedLicenseType(int activatedLicenseType) {
        this.activatedLicenseType = activatedLicenseType;
    }
}
