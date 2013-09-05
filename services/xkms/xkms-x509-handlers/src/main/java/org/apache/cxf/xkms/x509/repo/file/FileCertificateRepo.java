begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|x509
operator|.
name|repo
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|exception
operator|.
name|XKMSConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMajorEnum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMinorEnum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|UseKeyWithType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|x509
operator|.
name|repo
operator|.
name|CertificateRepo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|FileCertificateRepo
implements|implements
name|CertificateRepo
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FileCertificateRepo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CN_PREFIX
init|=
literal|"cn="
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TRUSTED_CAS_PATH
init|=
literal|"trusted_cas"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CAS_PATH
init|=
literal|"cas"
decl_stmt|;
specifier|private
specifier|final
name|File
name|storageDir
decl_stmt|;
specifier|private
specifier|final
name|CertificateFactory
name|certFactory
decl_stmt|;
specifier|public
name|FileCertificateRepo
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|storageDir
operator|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|certFactory
operator|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|saveCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|UseKeyWithType
name|id
parameter_list|)
block|{
name|saveCategorizedCertificate
argument_list|(
name|cert
argument_list|,
name|id
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|saveTrustedCACertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|UseKeyWithType
name|id
parameter_list|)
block|{
name|saveCategorizedCertificate
argument_list|(
name|cert
argument_list|,
name|id
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|saveCACertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|UseKeyWithType
name|id
parameter_list|)
block|{
name|saveCategorizedCertificate
argument_list|(
name|cert
argument_list|,
name|id
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|saveCategorizedCertificate
parameter_list|(
name|X509Certificate
name|cert
parameter_list|,
name|UseKeyWithType
name|id
parameter_list|,
name|boolean
name|isTrustedCA
parameter_list|,
name|boolean
name|isCA
parameter_list|)
block|{
name|String
name|name
init|=
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|category
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|isTrustedCA
condition|)
block|{
name|category
operator|=
name|TRUSTED_CAS_PATH
expr_stmt|;
block|}
if|if
condition|(
name|isCA
condition|)
block|{
name|category
operator|=
name|CAS_PATH
expr_stmt|;
block|}
try|try
block|{
name|File
name|certFile
init|=
operator|new
name|File
argument_list|(
name|storageDir
operator|+
literal|"/"
operator|+
name|category
argument_list|,
name|getRelativePathForSubjectDn
argument_list|(
name|cert
argument_list|)
argument_list|)
decl_stmt|;
name|certFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|BufferedOutputStream
name|bos
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|fos
argument_list|)
decl_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|cert
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error saving certificate "
operator|+
name|name
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|convertDnForFileSystem
parameter_list|(
name|String
name|dn
parameter_list|)
block|{
name|String
name|result
init|=
name|dn
operator|.
name|replace
argument_list|(
literal|"="
argument_list|,
literal|"-"
argument_list|)
decl_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|", "
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|","
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"/"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"\\"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"{"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"}"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|":"
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|String
name|getRelativePathForSubjectDn
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|BigInteger
name|serialNumber
init|=
name|cert
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
name|String
name|issuer
init|=
name|cert
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|convertDnForFileSystem
argument_list|(
name|issuer
argument_list|)
operator|+
literal|"-"
operator|+
name|serialNumber
operator|.
name|toString
argument_list|()
operator|+
literal|".cer"
decl_stmt|;
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[a-zA-Z_0-9-_]"
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
name|path
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|path
argument_list|,
literal|"Input did not match [a-zA-Z_0-9-_]."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|File
index|[]
name|getX509Files
parameter_list|()
block|{
name|List
argument_list|<
name|File
argument_list|>
name|certificateFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|certificateFiles
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|storageDir
operator|.
name|listFiles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|certificateFiles
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|File
argument_list|(
name|storageDir
operator|+
literal|"/"
operator|+
name|TRUSTED_CAS_PATH
argument_list|)
operator|.
name|listFiles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|certificateFiles
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|File
argument_list|(
name|storageDir
operator|+
literal|"/"
operator|+
name|CAS_PATH
argument_list|)
operator|.
name|listFiles
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|//
block|}
if|if
condition|(
name|certificateFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XKMSConfigurationException
argument_list|(
name|ResultMajorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_RECEIVER
argument_list|,
name|ResultMinorEnum
operator|.
name|HTTP_WWW_W_3_ORG_2002_03_XKMS_FAILURE
argument_list|,
literal|"File base persistence storage is not found: "
operator|+
name|storageDir
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|certificateFiles
operator|.
name|toArray
argument_list|(
operator|new
name|File
index|[
name|certificateFiles
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|X509Certificate
name|readCertificate
parameter_list|(
name|File
name|certFile
parameter_list|)
throws|throws
name|CertificateException
throws|,
name|FileNotFoundException
block|{
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
return|return
operator|(
name|X509Certificate
operator|)
name|certFactory
operator|.
name|generateCertificate
argument_list|(
name|fis
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getTrustedCaCerts
parameter_list|()
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
name|File
index|[]
name|list
init|=
name|getX509Files
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|certFile
range|:
name|list
control|)
block|{
try|try
block|{
if|if
condition|(
name|certFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|certFile
operator|.
name|getParent
argument_list|()
operator|.
name|endsWith
argument_list|(
name|TRUSTED_CAS_PATH
argument_list|)
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|readCertificate
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load certificate from file: %s. Error: %s"
argument_list|,
name|certFile
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|getCaCerts
parameter_list|()
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
name|File
index|[]
name|list
init|=
name|getX509Files
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|certFile
range|:
name|list
control|)
block|{
try|try
block|{
if|if
condition|(
name|certFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|certFile
operator|.
name|getParent
argument_list|()
operator|.
name|endsWith
argument_list|(
name|CAS_PATH
argument_list|)
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|readCertificate
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load certificate from file: %s. Error: %s"
argument_list|,
name|certFile
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findByServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
return|return
name|findBySubjectDn
argument_list|(
name|CN_PREFIX
operator|+
name|serviceName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findBySubjectDn
parameter_list|(
name|String
name|subjectDn
parameter_list|)
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
name|File
index|[]
name|list
init|=
name|getX509Files
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|certFile
range|:
name|list
control|)
block|{
try|try
block|{
if|if
condition|(
name|certFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|X509Certificate
name|cert
init|=
name|readCertificate
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for "
operator|+
name|subjectDn
operator|+
literal|". Checking cert "
operator|+
name|cert
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|", "
operator|+
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subjectDn
operator|.
name|equalsIgnoreCase
argument_list|(
name|cert
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|subjectDn
operator|.
name|equalsIgnoreCase
argument_list|(
name|cert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load certificate from file: %s. Error: %s"
argument_list|,
name|certFile
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
name|findByIssuerSerial
parameter_list|(
name|String
name|issuer
parameter_list|,
name|String
name|serial
parameter_list|)
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|()
decl_stmt|;
name|File
index|[]
name|list
init|=
name|getX509Files
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|certFile
range|:
name|list
control|)
block|{
try|try
block|{
if|if
condition|(
name|certFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|X509Certificate
name|cert
init|=
name|readCertificate
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|BigInteger
name|cs
init|=
name|cert
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
name|BigInteger
name|ss
init|=
operator|new
name|BigInteger
argument_list|(
name|serial
argument_list|,
literal|16
argument_list|)
decl_stmt|;
if|if
condition|(
name|issuer
operator|.
name|equalsIgnoreCase
argument_list|(
name|cert
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|cs
operator|.
name|equals
argument_list|(
name|ss
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot load certificate from file: %s. Error: %s"
argument_list|,
name|certFile
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

