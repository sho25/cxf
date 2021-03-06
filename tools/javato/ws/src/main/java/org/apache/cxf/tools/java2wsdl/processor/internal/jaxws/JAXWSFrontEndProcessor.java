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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|GenericArrayType
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
name|Modifier
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
name|ParameterizedType
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
name|jws
operator|.
name|WebService
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
name|i18n
operator|.
name|Message
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
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|PackageUtils
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
name|EndpointInfo
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
name|InterfaceInfo
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
name|OperationInfo
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
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|common
operator|.
name|Processor
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
name|common
operator|.
name|ToolConstants
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
name|common
operator|.
name|ToolContext
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
name|common
operator|.
name|ToolException
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
name|common
operator|.
name|model
operator|.
name|JavaException
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
name|common
operator|.
name|model
operator|.
name|JavaInterface
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
name|common
operator|.
name|model
operator|.
name|JavaMethod
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
name|common
operator|.
name|model
operator|.
name|JavaModel
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
name|common
operator|.
name|model
operator|.
name|JavaParameter
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
name|common
operator|.
name|model
operator|.
name|JavaReturn
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
name|common
operator|.
name|model
operator|.
name|JavaType
operator|.
name|Style
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
name|java2ws
operator|.
name|util
operator|.
name|JavaFirstUtil
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|AntGenerator
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|generator
operator|.
name|JaxwsClientGenerator
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|generator
operator|.
name|JaxwsImplGenerator
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|generator
operator|.
name|JaxwsSEIGenerator
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
operator|.
name|generator
operator|.
name|JaxwsServerGenerator
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
name|wsdlto
operator|.
name|core
operator|.
name|AbstractGenerator
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
name|wsdl
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
import|;
end_import

begin_class
specifier|public
class|class
name|JAXWSFrontEndProcessor
implements|implements
name|Processor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SEI_SUFFIX
init|=
literal|"_PortType"
decl_stmt|;
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
name|JAXWSFrontEndProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ToolContext
name|context
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AbstractGenerator
argument_list|>
name|generators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|infList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|checkJaxwsClass
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|services
init|=
operator|(
name|List
argument_list|<
name|ServiceInfo
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|SERVICE_LIST
argument_list|)
decl_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|JavaInterface
name|jinf
init|=
name|JavaFirstUtil
operator|.
name|serviceInfo2JavaInf
argument_list|(
name|serviceInfo
argument_list|)
decl_stmt|;
name|String
name|className
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|IMPL_CLASS
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|!=
literal|null
operator|&&
name|className
operator|.
name|equals
argument_list|(
name|jinf
operator|.
name|getFullClassName
argument_list|()
argument_list|)
condition|)
block|{
name|jinf
operator|.
name|setName
argument_list|(
name|jinf
operator|.
name|getName
argument_list|()
operator|+
name|SEI_SUFFIX
argument_list|)
expr_stmt|;
block|}
name|JavaModel
name|jm
init|=
operator|new
name|JavaModel
argument_list|()
decl_stmt|;
name|jm
operator|.
name|addInterface
argument_list|(
literal|"inf"
argument_list|,
name|jinf
argument_list|)
expr_stmt|;
name|jinf
operator|.
name|setJavaModel
argument_list|(
name|jm
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|JavaModel
operator|.
name|class
argument_list|,
name|jm
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|SERVICE_NAME
argument_list|,
name|serviceInfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|serviceInfo
operator|.
name|getEndpoints
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|PORT_NAME
argument_list|,
name|endpointInfo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|new
name|JaxwsSEIGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|new
name|JaxwsImplGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|new
name|JaxwsServerGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|new
name|JaxwsClientGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
operator|new
name|AntGenerator
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AbstractGenerator
name|generator
range|:
name|generators
control|)
block|{
name|generator
operator|.
name|generate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|ToolContext
name|env
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|JavaInterface
name|serviceInfo2JavaInf
parameter_list|(
name|ServiceInfo
name|service
parameter_list|)
block|{
name|JavaInterface
name|javaInf
init|=
operator|new
name|JavaInterface
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|inf
init|=
name|service
operator|.
name|getInterface
argument_list|()
decl_stmt|;
for|for
control|(
name|OperationInfo
name|op
range|:
name|inf
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|JavaMethod
name|jm
init|=
operator|new
name|JavaMethod
argument_list|()
decl_stmt|;
name|Method
name|m
init|=
operator|(
name|Method
operator|)
name|op
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|METHOD
argument_list|)
decl_stmt|;
name|jm
operator|.
name|setName
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Type
name|type
range|:
name|m
operator|.
name|getGenericParameterTypes
argument_list|()
control|)
block|{
name|JavaParameter
name|jp
init|=
operator|new
name|JavaParameter
argument_list|()
decl_stmt|;
name|jp
operator|.
name|setClassName
argument_list|(
name|getClassName
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|jp
operator|.
name|setStyle
argument_list|(
name|Style
operator|.
name|IN
argument_list|)
expr_stmt|;
name|jp
operator|.
name|setName
argument_list|(
literal|"arg"
operator|+
name|i
operator|++
argument_list|)
expr_stmt|;
name|jm
operator|.
name|addParameter
argument_list|(
name|jp
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Type
name|type
range|:
name|m
operator|.
name|getGenericExceptionTypes
argument_list|()
control|)
block|{
name|JavaException
name|jex
init|=
operator|new
name|JavaException
argument_list|()
decl_stmt|;
name|String
name|className
init|=
name|getClassName
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|jex
operator|.
name|setClassName
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|jex
operator|.
name|setName
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|jm
operator|.
name|addException
argument_list|(
name|jex
argument_list|)
expr_stmt|;
block|}
name|JavaReturn
name|jreturn
init|=
operator|new
name|JavaReturn
argument_list|()
decl_stmt|;
name|jreturn
operator|.
name|setClassName
argument_list|(
name|getClassName
argument_list|(
name|m
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jreturn
operator|.
name|setStyle
argument_list|(
name|Style
operator|.
name|OUT
argument_list|)
expr_stmt|;
name|jm
operator|.
name|setReturn
argument_list|(
name|jreturn
argument_list|)
expr_stmt|;
name|String
name|pkg
init|=
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|m
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
decl_stmt|;
name|javaInf
operator|.
name|setPackageName
argument_list|(
name|pkg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|pkg
else|:
name|ToolConstants
operator|.
name|DEFAULT_PACKAGE_NAME
argument_list|)
expr_stmt|;
name|javaInf
operator|.
name|addMethod
argument_list|(
name|jm
argument_list|)
expr_stmt|;
name|javaInf
operator|.
name|setName
argument_list|(
name|inf
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|jm
operator|.
name|getParameterList
argument_list|()
expr_stmt|;
block|}
return|return
name|javaInf
return|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
decl_stmt|;
if|if
condition|(
name|clz
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
name|clz
operator|.
name|getComponentType
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[]"
return|;
block|}
return|return
name|clz
operator|.
name|getName
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|type
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
return|return
name|type
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|""
return|;
block|}
specifier|public
name|void
name|checkJaxwsClass
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|context
operator|.
name|get
argument_list|(
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebService
name|webServiceAnno
init|=
name|clz
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webServiceAnno
operator|==
literal|null
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"CLASS_DOESNOT_CARRY_WEBSERVICE_ANNO"
argument_list|,
name|LOG
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|isImplRmiRemote
argument_list|(
name|clz
argument_list|)
condition|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"PARA_OR_RETURN_IMPL_REMOTE"
argument_list|,
name|LOG
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ToolException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|isImplRmiRemote
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|claz
parameter_list|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|claz
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
operator|!
literal|"java.lang.Object"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paraClasses
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
range|:
name|paraClasses
control|)
block|{
name|getInfClass
argument_list|(
name|clz
argument_list|)
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|returnClass
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|getInfClass
argument_list|(
name|returnClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|infList
operator|.
name|contains
argument_list|(
literal|"java.rmi.Remote"
argument_list|)
return|;
block|}
specifier|private
name|void
name|getInfClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|claz
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|inf
range|:
name|claz
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|getInfClass
argument_list|(
name|inf
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claz
operator|.
name|getSuperclass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getInfClass
argument_list|(
name|claz
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claz
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|infList
operator|.
name|add
argument_list|(
name|claz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

