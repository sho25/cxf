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
name|bus
operator|.
name|spring
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanDefinitionStoreException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|BeanDefinitionRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|UrlResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|support
operator|.
name|EncodedResource
import|;
end_import

begin_comment
comment|/**  * CXF reads a series of Spring XML files as part of initialization.  * The time it takes to parse them, especially if validating, builds up.  * The XML files shipped in a release in the JARs are valid and invariant.  * To speed things up, this class implements two levels of optimization.  * When a CXF distribution is fully-packaged, each of the Spring XML   * bus extension .xml files is accompanied by a FastInfoset '.fixml' file.  * These read much more rapidly. When one of those is present, this classs  * reads it instead of reading the XML text file.   *   * Absent a .fixml file, this class uses WoodStox instead of Xerces (or  * whatever the JDK is providing).  *   * The Woodstox optimization also applies to user cxf.xml or cxf-servlet.xml files  * if the user has disabled XML validation of Spring files with  * the org.apache.cxf.spring.validation.mode system property.  *   * Note that the fastInfoset optimization is only applied for the   * methods here that start from a Resource. If this is called with an InputSource,  * that optimization is not applied, since we can't reliably know the  * location of the XML.   */
end_comment

begin_class
specifier|public
class|class
name|ControlledValidationXmlBeanDefinitionReader
extends|extends
name|XmlBeanDefinitionReader
block|{
comment|/**      * Exception class used to avoid reading old FastInfoset files.      */
specifier|private
specifier|static
class|class
name|StaleFastinfosetException
extends|extends
name|Exception
block|{      }
comment|// the following flag allows performance comparisons with and
comment|// without fast infoset processing.
specifier|private
name|boolean
name|noFastinfoset
decl_stmt|;
comment|// Spring has no 'getter' for this, so we need our own copy.
specifier|private
name|int
name|visibleValidationMode
init|=
name|VALIDATION_AUTO
decl_stmt|;
comment|// We need a reference to the subclass.
specifier|private
name|TunedDocumentLoader
name|tunedDocumentLoader
decl_stmt|;
comment|/**      * @param beanFactory      */
specifier|public
name|ControlledValidationXmlBeanDefinitionReader
parameter_list|(
name|BeanDefinitionRegistry
name|beanFactory
parameter_list|)
block|{
name|super
argument_list|(
name|beanFactory
argument_list|)
expr_stmt|;
name|tunedDocumentLoader
operator|=
operator|new
name|TunedDocumentLoader
argument_list|()
expr_stmt|;
name|this
operator|.
name|setDocumentLoader
argument_list|(
name|tunedDocumentLoader
argument_list|)
expr_stmt|;
name|noFastinfoset
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.nofastinfoset"
argument_list|)
operator|!=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|doLoadBeanDefinitions
parameter_list|(
name|InputSource
name|inputSource
parameter_list|,
name|Resource
name|resource
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
comment|// sadly, the Spring class we are extending has the critical function
comment|// getValidationModeForResource
comment|// marked private instead of protected, so trickery is called for here.
name|boolean
name|suppressValidation
init|=
literal|false
decl_stmt|;
try|try
block|{
name|URL
name|url
init|=
name|resource
operator|.
name|getURL
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|.
name|getFile
argument_list|()
operator|.
name|contains
argument_list|(
literal|"META-INF/cxf/"
argument_list|)
condition|)
block|{
name|suppressValidation
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// this space intentionally left blank.
block|}
name|int
name|savedValidation
init|=
name|visibleValidationMode
decl_stmt|;
if|if
condition|(
name|suppressValidation
condition|)
block|{
name|setValidationMode
argument_list|(
name|VALIDATION_NONE
argument_list|)
expr_stmt|;
block|}
name|int
name|r
init|=
name|super
operator|.
name|doLoadBeanDefinitions
argument_list|(
name|inputSource
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|setValidationMode
argument_list|(
name|savedValidation
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setValidationMode
parameter_list|(
name|int
name|validationMode
parameter_list|)
block|{
name|visibleValidationMode
operator|=
name|validationMode
expr_stmt|;
name|super
operator|.
name|setValidationMode
argument_list|(
name|validationMode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|loadBeanDefinitions
parameter_list|(
name|EncodedResource
name|encodedResource
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
if|if
condition|(
name|noFastinfoset
condition|)
block|{
return|return
name|super
operator|.
name|loadBeanDefinitions
argument_list|(
name|encodedResource
argument_list|)
return|;
block|}
try|try
block|{
return|return
name|fastInfosetLoadBeanDefinitions
argument_list|(
name|encodedResource
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|super
operator|.
name|loadBeanDefinitions
argument_list|(
name|encodedResource
argument_list|)
return|;
block|}
block|}
specifier|private
name|int
name|fastInfosetLoadBeanDefinitions
parameter_list|(
name|EncodedResource
name|encodedResource
parameter_list|)
throws|throws
name|IOException
throws|,
name|StaleFastinfosetException
throws|,
name|ParserConfigurationException
throws|,
name|XMLStreamException
block|{
name|URL
name|resUrl
init|=
name|encodedResource
operator|.
name|getResource
argument_list|()
operator|.
name|getURL
argument_list|()
decl_stmt|;
comment|// There are XML files scampering around that don't end in .xml.
comment|// We don't apply the optimization to them.
if|if
condition|(
operator|!
name|resUrl
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|StaleFastinfosetException
argument_list|()
throw|;
block|}
name|String
name|fixmlPath
init|=
name|resUrl
operator|.
name|getPath
argument_list|()
operator|.
name|replaceFirst
argument_list|(
literal|"\\.xml$"
argument_list|,
literal|".fixml"
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
name|resUrl
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
comment|// beware of the relative URL rules for jar:, which are surprising.
if|if
condition|(
literal|"jar"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
name|fixmlPath
operator|=
name|fixmlPath
operator|.
name|replaceFirst
argument_list|(
literal|"^.*!"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|URL
name|fixmlUrl
init|=
operator|new
name|URL
argument_list|(
name|resUrl
argument_list|,
name|fixmlPath
argument_list|)
decl_stmt|;
comment|// if we are in unpacked files, we take some extra time
comment|// to ensure that we aren't using a stale Fastinfoset file.
if|if
condition|(
literal|"file"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
name|URLConnection
name|resCon
init|=
literal|null
decl_stmt|;
name|URLConnection
name|fixCon
init|=
literal|null
decl_stmt|;
name|resCon
operator|=
name|resUrl
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|fixCon
operator|=
name|fixmlUrl
operator|.
name|openConnection
argument_list|()
expr_stmt|;
if|if
condition|(
name|resCon
operator|.
name|getLastModified
argument_list|()
operator|>
name|fixCon
operator|.
name|getLastModified
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|StaleFastinfosetException
argument_list|()
throw|;
block|}
block|}
name|Resource
name|newResource
init|=
operator|new
name|UrlResource
argument_list|(
name|fixmlUrl
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|tunedDocumentLoader
operator|.
name|loadFastinfosetDocument
argument_list|(
name|fixmlUrl
argument_list|)
decl_stmt|;
return|return
name|registerBeanDefinitions
argument_list|(
name|doc
argument_list|,
name|newResource
argument_list|)
return|;
block|}
block|}
end_class

end_unit

