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
name|transport
operator|.
name|http_jetty
operator|.
name|spring
package|;
end_package

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
name|List
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|BeanDefinitionBuilder
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JAXBHelper
block|{
specifier|private
name|JAXBHelper
parameter_list|()
block|{              }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|List
argument_list|<
name|V
argument_list|>
name|parseListElement
parameter_list|(
name|Element
name|parent
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|QName
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
throws|throws
name|JAXBException
block|{
name|List
argument_list|<
name|V
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
decl_stmt|;
name|NodeList
name|nl
init|=
name|parent
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|Node
name|data
init|=
literal|null
decl_stmt|;
name|JAXBContext
name|context
init|=
literal|null
decl_stmt|;
name|String
name|pkg
init|=
literal|""
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|c
operator|&&
name|c
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pkg
operator|=
name|c
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|pkg
argument_list|,
name|c
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|n
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
operator|&&
name|name
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|data
operator|=
name|n
expr_stmt|;
name|Object
name|obj
init|=
name|unmarshal
argument_list|(
name|context
argument_list|,
name|data
argument_list|,
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
operator|(
name|V
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|parseElement
parameter_list|(
name|Element
name|element
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
throws|throws
name|JAXBException
block|{
if|if
condition|(
literal|null
operator|==
name|element
condition|)
block|{
return|return
literal|null
return|;
block|}
name|JAXBContext
name|context
init|=
literal|null
decl_stmt|;
name|String
name|pkg
init|=
literal|""
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|c
operator|&&
name|c
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pkg
operator|=
name|c
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|pkg
argument_list|,
name|c
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|pkg
argument_list|)
expr_stmt|;
block|}
name|Object
name|obj
init|=
name|unmarshal
argument_list|(
name|context
argument_list|,
name|element
argument_list|,
name|c
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Object
name|unmarshal
parameter_list|(
name|JAXBContext
name|context
parameter_list|,
name|Node
name|data
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|obj
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Unmarshaller
name|u
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|obj
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|data
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
literal|"Could not parse configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|obj
return|;
block|}
block|}
end_class

end_unit

