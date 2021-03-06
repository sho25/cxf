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
name|binding
operator|.
name|corba
operator|.
name|types
package|;
end_package

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
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|corba
operator|.
name|CorbaTypeMap
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|Exception
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
name|binding
operator|.
name|corba
operator|.
name|wsdl
operator|.
name|MemberType
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaExceptionListener
extends|extends
name|AbstractCorbaTypeListener
block|{
specifier|private
specifier|final
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|private
specifier|final
name|ORB
name|orb
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|MemberType
argument_list|>
name|exMembers
decl_stmt|;
specifier|private
name|int
name|memberCount
decl_stmt|;
specifier|private
name|CorbaTypeListener
name|currentTypeListener
decl_stmt|;
specifier|private
name|QName
name|memberElement
decl_stmt|;
specifier|private
name|ServiceInfo
name|sInfo
decl_stmt|;
specifier|public
name|CorbaExceptionListener
parameter_list|(
name|CorbaObjectHandler
name|handler
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|,
name|ORB
name|orbRef
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|orb
operator|=
name|orbRef
expr_stmt|;
name|typeMap
operator|=
name|map
expr_stmt|;
name|sInfo
operator|=
name|serviceInfo
expr_stmt|;
name|exMembers
operator|=
operator|(
operator|(
name|Exception
operator|)
name|handler
operator|.
name|getType
argument_list|()
operator|)
operator|.
name|getMember
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|processStartElement
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
comment|//REVISIT, assume only elements not attrs
if|if
condition|(
name|currentTypeListener
operator|==
literal|null
condition|)
block|{
name|memberElement
operator|=
name|name
expr_stmt|;
name|currentTypeListener
operator|=
name|CorbaHandlerUtils
operator|.
name|getTypeListener
argument_list|(
name|name
argument_list|,
name|exMembers
operator|.
name|get
argument_list|(
name|memberCount
argument_list|)
operator|.
name|getIdltype
argument_list|()
argument_list|,
name|typeMap
argument_list|,
name|orb
argument_list|,
name|sInfo
argument_list|)
expr_stmt|;
name|currentTypeListener
operator|.
name|setNamespaceContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
operator|(
operator|(
name|CorbaExceptionHandler
operator|)
name|handler
operator|)
operator|.
name|addMember
argument_list|(
name|currentTypeListener
operator|.
name|getCorbaObject
argument_list|()
argument_list|)
expr_stmt|;
name|memberCount
operator|++
expr_stmt|;
block|}
else|else
block|{
name|currentTypeListener
operator|.
name|processStartElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|processEndElement
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
if|if
condition|(
name|currentTypeListener
operator|!=
literal|null
condition|)
block|{
name|currentTypeListener
operator|.
name|processEndElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|memberElement
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|currentTypeListener
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|processCharacters
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|currentTypeListener
operator|.
name|processCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|processWriteAttribute
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|currentTypeListener
operator|!=
literal|null
condition|)
block|{
name|currentTypeListener
operator|.
name|processWriteAttribute
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|processWriteNamespace
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
if|if
condition|(
name|currentTypeListener
operator|!=
literal|null
condition|)
block|{
name|currentTypeListener
operator|.
name|processWriteNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

