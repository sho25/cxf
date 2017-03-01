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
name|ws
operator|.
name|policy
operator|.
name|builder
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|JAXBContext
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
name|JAXBElement
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
name|JAXBException
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
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|Element
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBContextCache
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
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
name|common
operator|.
name|jaxb
operator|.
name|JAXBUtils
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|AssertionBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|AssertionBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|xml
operator|.
name|XMLPrimitiveAssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|JaxbAssertionBuilder
parameter_list|<
name|T
parameter_list|>
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JaxbAssertionBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|QName
argument_list|>
name|supportedTypes
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
specifier|private
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
decl_stmt|;
comment|/**      * Constructs a JAXBAssertionBuilder from the QName of the schema type      * @param qn the schema type      * @throws JAXBException      * @throws ClassNotFoundException      */
specifier|public
name|JaxbAssertionBuilder
parameter_list|(
name|QName
name|qn
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|ClassNotFoundException
block|{
name|this
argument_list|(
name|JAXBUtils
operator|.
name|namespaceURIToPackage
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|+
literal|"."
operator|+
name|JAXBUtils
operator|.
name|nameToIdentifier
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|JAXBUtils
operator|.
name|IdentifierType
operator|.
name|CLASS
argument_list|)
argument_list|,
name|qn
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs a JAXBAssertionBuilder from the specified class name and schema type.      * @param className the name of the class to which the schema type is mapped      * @param qn the schema type      * @throws JAXBException      * @throws ClassNotFoundException      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|JaxbAssertionBuilder
parameter_list|(
name|String
name|className
parameter_list|,
name|QName
name|qn
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|ClassNotFoundException
block|{
name|this
argument_list|(
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|className
argument_list|,
name|JaxbAssertionBuilder
operator|.
name|class
argument_list|)
argument_list|,
name|qn
argument_list|)
expr_stmt|;
block|}
comment|/**     * Constructs a JAXBAssertionBuilder from the specified class and schema type.     * @param type the class to which the schema type is mapped     * @param qn the schema type     * @throws JAXBException     * @throws ClassNotFoundException     */
specifier|public
name|JaxbAssertionBuilder
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|QName
name|qn
parameter_list|)
throws|throws
name|JAXBException
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|supportedTypes
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|==
literal|null
operator|||
name|classes
operator|==
literal|null
condition|)
block|{
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|classes
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|context
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|protected
name|Unmarshaller
name|getUnmarshaller
parameter_list|()
block|{
try|try
block|{
name|Unmarshaller
name|um
init|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|um
operator|.
name|setEventHandler
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|um
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
name|JaxbAssertion
argument_list|<
name|T
argument_list|>
name|assertion
init|=
name|buildAssertion
argument_list|()
decl_stmt|;
name|assertion
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setOptional
argument_list|(
name|XMLPrimitiveAssertionBuilder
operator|.
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setIgnorable
argument_list|(
name|XMLPrimitiveAssertionBuilder
operator|.
name|isIgnorable
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|getData
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|assertion
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|supportedTypes
operator|.
name|toArray
argument_list|(
operator|new
name|QName
index|[
name|supportedTypes
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|protected
name|JaxbAssertion
argument_list|<
name|T
argument_list|>
name|buildAssertion
parameter_list|()
block|{
return|return
operator|new
name|JaxbAssertion
argument_list|<
name|T
argument_list|>
argument_list|()
return|;
block|}
specifier|protected
name|boolean
name|getOptionality
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|boolean
name|optional
init|=
literal|false
decl_stmt|;
name|String
name|value
init|=
name|element
operator|.
name|getAttributeNS
argument_list|(
name|Constants
operator|.
name|Q_ELEM_OPTIONAL_ATTR
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|Constants
operator|.
name|Q_ELEM_OPTIONAL_ATTR
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|optional
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|optional
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|T
name|getData
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|Object
name|obj
init|=
literal|null
decl_stmt|;
name|Unmarshaller
name|um
init|=
name|getUnmarshaller
argument_list|()
decl_stmt|;
try|try
block|{
name|obj
operator|=
name|um
operator|.
name|unmarshal
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"UNMARSHAL_ELEMENT_EXC"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|JAXBUtils
operator|.
name|closeUnmarshaller
argument_list|(
name|um
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
name|obj
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|obj
operator|&&
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Unmarshaled element into object of type: "
operator|+
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"    value: "
operator|+
name|obj
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|obj
return|;
block|}
block|}
end_class

end_unit

