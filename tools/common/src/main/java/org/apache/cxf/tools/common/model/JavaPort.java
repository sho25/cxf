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
name|common
operator|.
name|model
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
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
specifier|public
class|class
name|JavaPort
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|portType
decl_stmt|;
specifier|private
name|String
name|bindingName
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|operations
init|=
operator|new
name|ArrayList
argument_list|<
name|JavaMethod
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|private
name|String
name|soapVersion
decl_stmt|;
specifier|private
name|SOAPBinding
operator|.
name|Style
name|style
decl_stmt|;
specifier|private
name|String
name|transURI
decl_stmt|;
specifier|private
name|String
name|interfaceClass
decl_stmt|;
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|portName
decl_stmt|;
specifier|private
name|String
name|methodName
decl_stmt|;
specifier|private
name|String
name|javadoc
decl_stmt|;
specifier|public
name|JavaPort
parameter_list|(
name|String
name|pname
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|pname
expr_stmt|;
block|}
specifier|public
name|void
name|setJavaDoc
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|javadoc
operator|=
name|JavaInterface
operator|.
name|formatJavaDoc
argument_list|(
name|d
argument_list|,
literal|"     "
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getJavaDoc
parameter_list|()
block|{
return|return
name|javadoc
operator|==
literal|null
condition|?
literal|""
else|:
name|javadoc
return|;
block|}
specifier|public
name|void
name|setTransURI
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|transURI
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|String
name|getTransURI
parameter_list|()
block|{
return|return
name|this
operator|.
name|transURI
return|;
block|}
specifier|public
name|void
name|setStyle
parameter_list|(
name|SOAPBinding
operator|.
name|Style
name|sty
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|sty
expr_stmt|;
block|}
specifier|public
name|SOAPBinding
operator|.
name|Style
name|getStyle
parameter_list|()
block|{
return|return
name|this
operator|.
name|style
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|portname
parameter_list|)
block|{
name|name
operator|=
name|portname
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setPortType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|portType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getPortType
parameter_list|()
block|{
return|return
name|portType
return|;
block|}
specifier|public
name|void
name|setBindingName
parameter_list|(
name|String
name|bName
parameter_list|)
block|{
name|this
operator|.
name|bindingName
operator|=
name|bName
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingName
parameter_list|()
block|{
return|return
name|bindingName
return|;
block|}
specifier|public
name|void
name|addOperation
parameter_list|(
name|JavaMethod
name|method
parameter_list|)
block|{
name|operations
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|JavaMethod
argument_list|>
name|getOperations
parameter_list|()
block|{
return|return
name|operations
return|;
block|}
specifier|public
name|void
name|setBindingAdress
parameter_list|(
name|String
name|add
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|add
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingAdress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|void
name|setSoapVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|soapVersion
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getSoapVersion
parameter_list|()
block|{
return|return
name|soapVersion
return|;
block|}
specifier|public
name|void
name|setPackageName
parameter_list|(
name|String
name|pkgName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|pkgName
expr_stmt|;
block|}
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|this
operator|.
name|packageName
return|;
block|}
specifier|public
name|String
name|getInterfaceClass
parameter_list|()
block|{
return|return
name|this
operator|.
name|interfaceClass
return|;
block|}
specifier|public
name|void
name|setInterfaceClass
parameter_list|(
name|String
name|clzname
parameter_list|)
block|{
name|this
operator|.
name|interfaceClass
operator|=
name|clzname
expr_stmt|;
block|}
specifier|public
name|void
name|setNameSpace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|ns
expr_stmt|;
block|}
specifier|public
name|String
name|getNameSpace
parameter_list|()
block|{
return|return
name|this
operator|.
name|namespace
return|;
block|}
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|pname
parameter_list|)
block|{
name|portName
operator|=
name|pname
expr_stmt|;
block|}
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|mname
parameter_list|)
block|{
name|methodName
operator|=
name|mname
expr_stmt|;
block|}
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
if|if
condition|(
name|methodName
operator|==
literal|null
condition|)
block|{
return|return
literal|"get"
operator|+
name|getName
argument_list|()
return|;
block|}
return|return
name|methodName
return|;
block|}
specifier|public
name|String
name|getFullClassName
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getPackageName
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|getPackageName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|getInterfaceClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

