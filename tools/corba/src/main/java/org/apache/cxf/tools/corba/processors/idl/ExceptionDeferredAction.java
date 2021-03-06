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
name|tools
operator|.
name|corba
operator|.
name|processors
operator|.
name|idl
package|;
end_package

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
name|CorbaTypeImpl
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
name|tools
operator|.
name|corba
operator|.
name|common
operator|.
name|ReferenceConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaType
import|;
end_import

begin_class
specifier|public
class|class
name|ExceptionDeferredAction
implements|implements
name|SchemaDeferredAction
block|{
specifier|protected
name|MemberType
name|member
decl_stmt|;
specifier|protected
name|XmlSchemaElement
name|element
decl_stmt|;
specifier|public
name|ExceptionDeferredAction
parameter_list|(
name|MemberType
name|memberType
parameter_list|,
name|XmlSchemaElement
name|elem
parameter_list|)
block|{
name|member
operator|=
name|memberType
expr_stmt|;
name|element
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|ExceptionDeferredAction
parameter_list|(
name|MemberType
name|memberType
parameter_list|)
block|{
name|member
operator|=
name|memberType
expr_stmt|;
block|}
specifier|public
name|ExceptionDeferredAction
parameter_list|(
name|XmlSchemaElement
name|elem
parameter_list|)
block|{
name|element
operator|=
name|elem
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|XmlSchemaType
name|stype
parameter_list|,
name|CorbaTypeImpl
name|ctype
parameter_list|)
block|{
if|if
condition|(
name|member
operator|!=
literal|null
condition|)
block|{
name|member
operator|.
name|setIdltype
argument_list|(
name|ctype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setSchemaType
argument_list|(
name|stype
argument_list|)
expr_stmt|;
name|element
operator|.
name|setSchemaTypeName
argument_list|(
name|stype
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|stype
operator|.
name|getQName
argument_list|()
operator|.
name|equals
argument_list|(
name|ReferenceConstants
operator|.
name|WSADDRESSING_TYPE
argument_list|)
condition|)
block|{
name|element
operator|.
name|setNillable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

