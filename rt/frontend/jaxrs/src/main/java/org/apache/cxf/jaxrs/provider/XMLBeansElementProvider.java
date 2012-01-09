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
name|jaxrs
operator|.
name|provider
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlObject
import|;
end_import

begin_comment
comment|/**  * Provider for XMLBeans data objects.  */
end_comment

begin_class
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|}
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|XMLBeansElementProvider
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|XmlObject
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|XmlObject
argument_list|>
block|{
comment|/** {@inheritDoc} */
specifier|public
name|XmlObject
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|XmlObject
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parseXmlBean
argument_list|(
name|type
argument_list|,
name|is
argument_list|)
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|void
name|writeTo
parameter_list|(
name|XmlObject
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|entityStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// May need to set some XMLOptions here
name|t
operator|.
name|save
argument_list|(
name|entityStream
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|long
name|getSize
parameter_list|(
name|XmlObject
name|t
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
comment|// return length not known
return|return
operator|-
literal|1
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|isXmlBean
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|isXmlBean
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Create an XMLBean object from an XML stream.      *       * @param type declared type of the target object      * @param reader stream reader for the XML stream      * @return an XMLBean data object, or none if unable to process      */
specifier|protected
name|XmlObject
name|parseXmlBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|XmlObject
name|result
init|=
literal|null
decl_stmt|;
comment|// get XMLBeans inner class Factory
name|Class
argument_list|<
name|?
argument_list|>
name|factory
init|=
name|getFactory
argument_list|(
name|type
argument_list|)
decl_stmt|;
try|try
block|{
comment|// find and invoke method parse(InputStream)
name|Method
name|m
init|=
name|factory
operator|.
name|getMethod
argument_list|(
literal|"parse"
argument_list|,
name|reader
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|Object
index|[]
name|args
init|=
block|{
name|reader
block|}
decl_stmt|;
name|Object
name|obj
init|=
name|m
operator|.
name|invoke
argument_list|(
name|type
argument_list|,
name|args
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XmlObject
condition|)
block|{
name|result
operator|=
operator|(
name|XmlObject
operator|)
name|obj
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|nsme
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|iae
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_INTERNAL_ERROR
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Create an XMLBean data object from an<code>InputStream</code>      *       * @param type declared type of the required object      * @param inStream      * @return an XMLBean object if successful, otherwise null      */
specifier|protected
name|XmlObject
name|parseXmlBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|InputStream
name|inStream
parameter_list|)
block|{
name|XmlObject
name|result
init|=
literal|null
decl_stmt|;
name|Reader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
name|inStream
argument_list|)
decl_stmt|;
comment|// delegate to parseXmlBean(Class type, Reader reader)
name|result
operator|=
name|parseXmlBean
argument_list|(
name|type
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Create an XMLBean data object using a stream<code>Reader</code>      *       * @param type declared type of the desired XMLBean data object      * @param reader      * @return an instance of the required object, otherwise null      */
specifier|protected
name|XmlObject
name|parseXmlBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Reader
name|reader
parameter_list|)
block|{
name|XmlObject
name|result
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|factory
init|=
name|getFactory
argument_list|(
name|type
argument_list|)
decl_stmt|;
try|try
block|{
comment|// get factory method parse(InputStream)
name|Method
name|m
init|=
name|factory
operator|.
name|getMethod
argument_list|(
literal|"parse"
argument_list|,
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Object
index|[]
name|args
init|=
block|{
name|reader
block|}
decl_stmt|;
name|Object
name|obj
init|=
name|m
operator|.
name|invoke
argument_list|(
name|type
argument_list|,
name|args
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|XmlObject
condition|)
block|{
name|result
operator|=
operator|(
name|XmlObject
operator|)
name|obj
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|nsme
parameter_list|)
block|{
comment|// do nothing, just return null
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ite
parameter_list|)
block|{
comment|// do nothing, just return null
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|iae
parameter_list|)
block|{
comment|// do nothing, just return null
block|}
return|return
name|result
return|;
block|}
comment|/**      * Locate the XMLBean<code>Factory</code> inner class.      *       * @param type      * @return the Factory class if present, otherwise null.      */
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getFactory
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|result
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
name|type
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
comment|// look for XMLBeans inner class Factory
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|inter
range|:
name|interfaces
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|declared
init|=
name|inter
operator|.
name|getDeclaredClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|declared
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Factory"
argument_list|)
condition|)
block|{
name|result
operator|=
name|c
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * Check if a<code>Class</code> is a valid XMLBeans data object. The check procedure involves looking      * for the Interface<code>XmlObject</code> in the target type's declaration. Assumed to be sufficient      * to identify the type as an XMLBean. From the javadoc (2.3.0) for XmlObject: "Corresponds to the XML      * Schema xs:anyType, the base type for all XML Beans."      *       * @param type      * @return true if found to be an XMLBean object, otherwise false      */
specifier|protected
name|boolean
name|isXmlBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
block|{
name|type
block|}
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|interfaces
operator|=
name|type
operator|.
name|getInterfaces
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|i
range|:
name|interfaces
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|superInterfaces
init|=
name|i
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|superI
range|:
name|superInterfaces
control|)
block|{
if|if
condition|(
name|superI
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.xmlbeans.XmlObject"
argument_list|)
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

