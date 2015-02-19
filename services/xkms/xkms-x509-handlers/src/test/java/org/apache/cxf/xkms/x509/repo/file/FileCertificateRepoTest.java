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
name|BufferedReader
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
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
name|handlers
operator|.
name|Applications
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|FileCertificateRepoTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EXAMPLE_SUBJECT_DN
init|=
literal|"CN=www.issuer.com, L=CGN, ST=NRW, C=DE, O=Issuer"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED_CERT_FILE_NAME
init|=
literal|"CN-www.issuer.com_L-CGN_ST-NRW_C-DE_O-Issuer.cer"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSaveAndFind
parameter_list|()
throws|throws
name|CertificateException
throws|,
name|IOException
block|{
name|File
name|storageDir
init|=
operator|new
name|File
argument_list|(
literal|"target/teststore1"
argument_list|)
decl_stmt|;
name|storageDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileCertificateRepo
name|fileRegisterHandler
init|=
operator|new
name|FileCertificateRepo
argument_list|(
literal|"target/teststore1"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/store1/"
operator|+
name|EXPECTED_CERT_FILE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can not find path "
operator|+
name|is
operator|+
literal|" in classpath"
argument_list|)
throw|;
block|}
name|X509Certificate
name|cert
init|=
name|loadTestCert
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|UseKeyWithType
name|key
init|=
operator|new
name|UseKeyWithType
argument_list|()
decl_stmt|;
name|key
operator|.
name|setApplication
argument_list|(
name|Applications
operator|.
name|PKIX
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setIdentifier
argument_list|(
name|EXAMPLE_SUBJECT_DN
argument_list|)
expr_stmt|;
name|fileRegisterHandler
operator|.
name|saveCertificate
argument_list|(
name|cert
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|File
name|certFile
init|=
operator|new
name|File
argument_list|(
name|storageDir
argument_list|,
name|EXPECTED_CERT_FILE_NAME
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Cert file "
operator|+
name|certFile
operator|+
literal|" should exist"
argument_list|,
name|certFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|certFile
argument_list|)
decl_stmt|;
name|X509Certificate
name|outCert
init|=
name|loadTestCert
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|cert
argument_list|,
name|outCert
argument_list|)
expr_stmt|;
name|X509Certificate
name|resultCert
init|=
name|fileRegisterHandler
operator|.
name|findBySubjectDn
argument_list|(
name|EXAMPLE_SUBJECT_DN
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|resultCert
argument_list|)
expr_stmt|;
block|}
specifier|private
name|X509Certificate
name|loadTestCert
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
block|{
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
return|return
operator|(
name|X509Certificate
operator|)
name|factory
operator|.
name|generateCertificate
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|private
name|String
name|read
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
block|{
name|StringBuilder
name|fileData
init|=
operator|new
name|StringBuilder
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|numRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|numRead
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|readData
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|numRead
argument_list|)
decl_stmt|;
name|fileData
operator|.
name|append
argument_list|(
name|readData
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|char
index|[
literal|1024
index|]
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|fileData
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|void
name|convertBase64ToCer
parameter_list|(
name|String
name|sourcePath
parameter_list|,
name|String
name|destPath
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|sourcePath
argument_list|)
decl_stmt|;
name|String
name|certString
init|=
name|read
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|byte
index|[]
name|certData
init|=
name|DatatypeConverter
operator|.
name|parseBase64Binary
argument_list|(
name|certString
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|destPath
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
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
name|certData
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
annotation|@
name|Test
specifier|public
name|void
name|testFindBySubjectName
parameter_list|()
throws|throws
name|CertificateException
block|{
name|File
name|storageDir
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/store1"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|storageDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|storageDir
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|FileCertificateRepo
name|persistenceManager
init|=
operator|new
name|FileCertificateRepo
argument_list|(
literal|"src/test/resources/store1"
argument_list|)
decl_stmt|;
name|X509Certificate
name|resCert
init|=
name|persistenceManager
operator|.
name|findBySubjectDn
argument_list|(
name|EXAMPLE_SUBJECT_DN
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|resCert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConvertDnForFileSystem
parameter_list|()
throws|throws
name|CertificateException
block|{
name|String
name|convertedName
init|=
operator|new
name|FileCertificateRepo
argument_list|(
literal|"src/test/resources/store1"
argument_list|)
operator|.
name|convertIdForFileSystem
argument_list|(
name|EXAMPLE_SUBJECT_DN
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"CN-www.issuer.com_L-CGN_ST-NRW_C-DE_O-Issuer"
argument_list|,
name|convertedName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

