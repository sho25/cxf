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
name|jaxws
operator|.
name|support
package|;
end_package

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
name|ResourceBundle
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
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
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|jaxb
operator|.
name|JAXBEncoderDecoder
import|;
end_import

begin_class
specifier|public
class|class
name|JaxWsImplementorInfo
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
name|JaxWsImplementorInfo
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|LOG
operator|.
name|getResourceBundle
argument_list|()
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|implementorClass
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|seiClass
decl_stmt|;
specifier|private
name|ParameterizedType
name|seiType
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WebService
argument_list|>
name|wsAnnotations
init|=
operator|new
name|ArrayList
argument_list|<
name|WebService
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|private
name|WebServiceProvider
name|wsProviderAnnotation
decl_stmt|;
specifier|public
name|JaxWsImplementorInfo
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|ic
parameter_list|)
block|{
name|implementorClass
operator|=
name|ic
expr_stmt|;
name|initialize
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getSEIClass
parameter_list|()
block|{
return|return
name|seiClass
return|;
block|}
specifier|public
name|ParameterizedType
name|getSEIType
parameter_list|()
block|{
return|return
name|seiType
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getImplementorClass
parameter_list|()
block|{
return|return
name|implementorClass
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEndpointClass
parameter_list|()
block|{
name|Class
name|endpointInterface
init|=
name|getSEIClass
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|endpointInterface
condition|)
block|{
name|endpointInterface
operator|=
name|getImplementorClass
argument_list|()
expr_stmt|;
block|}
return|return
name|endpointInterface
return|;
block|}
specifier|public
name|String
name|getWsdlLocation
parameter_list|()
block|{
for|for
control|(
name|WebService
name|service
range|:
name|wsAnnotations
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|wsdlLocation
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|service
operator|.
name|wsdlLocation
argument_list|()
return|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|wsProviderAnnotation
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|wsProviderAnnotation
operator|.
name|wsdlLocation
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|wsProviderAnnotation
operator|.
name|wsdlLocation
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * See use of targetNamespace in {@link WebService}.      *       * @return the qualified name of the service.      */
specifier|public
name|QName
name|getServiceName
parameter_list|()
block|{
name|String
name|serviceName
init|=
literal|null
decl_stmt|;
name|String
name|namespace
init|=
literal|null
decl_stmt|;
comment|// serviceName cannot be specified on SEI so check impl class only
if|if
condition|(
name|wsAnnotations
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|offset
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|seiClass
operator|==
literal|null
condition|)
block|{
name|offset
operator|=
literal|0
expr_stmt|;
block|}
comment|//traverse up the parent impl classes for this info as well, but
comment|//not the last one which would be the sei annotation
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|wsAnnotations
operator|.
name|size
argument_list|()
operator|-
name|offset
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|serviceName
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|wsAnnotations
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|serviceName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|namespace
operator|=
name|wsAnnotations
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|(
name|serviceName
operator|==
literal|null
operator|||
name|namespace
operator|==
literal|null
operator|)
operator|&&
name|wsProviderAnnotation
operator|!=
literal|null
condition|)
block|{
name|serviceName
operator|=
name|wsProviderAnnotation
operator|.
name|serviceName
argument_list|()
expr_stmt|;
name|namespace
operator|=
name|wsProviderAnnotation
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|serviceName
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|implementorClass
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"Service"
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|namespace
operator|=
name|getDefaultNamespace
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|serviceName
argument_list|)
return|;
block|}
comment|/**      * See use of targetNamespace in {@link WebService}.      *       * @return the qualified name of the endpoint.      */
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
name|String
name|portName
init|=
literal|null
decl_stmt|;
name|String
name|namespace
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
comment|// portName cannot be specified on SEI so check impl class only
if|if
condition|(
name|wsAnnotations
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|offset
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|seiClass
operator|==
literal|null
condition|)
block|{
name|offset
operator|=
literal|0
expr_stmt|;
block|}
comment|//traverse up the parent impl classes for this info as well, but
comment|//not the last one which would be the sei annotation
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|wsAnnotations
operator|.
name|size
argument_list|()
operator|-
name|offset
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|portName
argument_list|)
condition|)
block|{
name|portName
operator|=
name|wsAnnotations
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|portName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|namespace
operator|=
name|wsAnnotations
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|wsAnnotations
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|(
name|portName
operator|==
literal|null
operator|||
name|namespace
operator|==
literal|null
operator|)
operator|&&
name|wsProviderAnnotation
operator|!=
literal|null
condition|)
block|{
name|portName
operator|=
name|wsProviderAnnotation
operator|.
name|portName
argument_list|()
expr_stmt|;
name|namespace
operator|=
name|wsProviderAnnotation
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|portName
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|portName
operator|=
name|name
operator|+
literal|"Port"
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|portName
argument_list|)
condition|)
block|{
name|portName
operator|=
name|implementorClass
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"Port"
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|namespace
operator|=
name|getDefaultNamespace
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|portName
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getInterfaceName
parameter_list|()
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|namespace
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|seiClass
operator|!=
literal|null
condition|)
block|{
name|WebService
name|service
init|=
name|seiClass
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
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|name
operator|=
name|service
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|targetNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|namespace
operator|=
name|service
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|WebService
name|service
range|:
name|wsAnnotations
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|name
argument_list|()
argument_list|)
operator|&&
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|service
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|targetNamespace
argument_list|()
argument_list|)
operator|&&
name|namespace
operator|==
literal|null
condition|)
block|{
name|namespace
operator|=
name|service
operator|.
name|targetNamespace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|seiClass
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|seiClass
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|implementorClass
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|implementorClass
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|seiClass
operator|!=
literal|null
condition|)
block|{
name|namespace
operator|=
name|getDefaultNamespace
argument_list|(
name|seiClass
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|implementorClass
operator|!=
literal|null
condition|)
block|{
name|namespace
operator|=
name|getDefaultNamespace
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|private
name|String
name|getDefaultNamespace
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|String
name|pkg
init|=
name|PackageUtils
operator|.
name|getNamespace
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|clazz
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pkg
argument_list|)
condition|?
literal|"http://unknown.namespace/"
else|:
name|pkg
return|;
block|}
specifier|private
name|String
name|getWSInterfaceName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|implClz
parameter_list|)
block|{
if|if
condition|(
name|implClz
operator|.
name|isInterface
argument_list|()
operator|&&
name|implClz
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|implClz
operator|.
name|getName
argument_list|()
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|clzs
init|=
name|implClz
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
name|clz
range|:
name|clzs
control|)
block|{
if|if
condition|(
literal|null
operator|!=
name|clz
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|clz
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getImplementorClassName
parameter_list|()
block|{
for|for
control|(
name|WebService
name|service
range|:
name|wsAnnotations
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|service
operator|.
name|endpointInterface
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|service
operator|.
name|endpointInterface
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
specifier|static
name|boolean
name|ifAnnotationLoadedByOtherClassLoader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationClass
parameter_list|)
block|{
for|for
control|(
name|Annotation
name|an
range|:
name|cls
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|an
operator|.
name|annotationType
argument_list|()
operator|!=
literal|null
operator|&&
name|annotationClass
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|an
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|initialize
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|implementorClass
decl_stmt|;
while|while
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|WebService
name|annotation
init|=
name|cls
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
name|annotation
operator|!=
literal|null
condition|)
block|{
name|wsAnnotations
operator|.
name|add
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
if|if
condition|(
name|cls
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|cls
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// check if there are annotations has the same name with WebServices
if|if
condition|(
name|ifAnnotationLoadedByOtherClassLoader
argument_list|(
name|cls
argument_list|,
name|WebService
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"WEBSERVICE_ANNOTATIONS_IS_LOADED_BY_OTHER_CLASSLOADER"
argument_list|,
name|WebService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
name|cls
operator|=
name|cls
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|sei
init|=
name|getImplementorClassName
argument_list|()
decl_stmt|;
name|boolean
name|seiFromWsAnnotation
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|sei
argument_list|)
condition|)
block|{
name|seiFromWsAnnotation
operator|=
literal|false
expr_stmt|;
name|sei
operator|=
name|getWSInterfaceName
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|sei
argument_list|)
condition|)
block|{
try|try
block|{
name|seiClass
operator|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|sei
argument_list|,
name|implementorClass
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"SEI_LOAD_FAILURE_MSG"
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|WebService
name|seiAnnotation
init|=
name|seiClass
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
literal|null
operator|==
name|seiAnnotation
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"SEI_WITHOUT_WEBSERVICE_ANNOTATION_EXC"
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|seiFromWsAnnotation
operator|&&
operator|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|seiAnnotation
operator|.
name|portName
argument_list|()
argument_list|)
operator|||
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|seiAnnotation
operator|.
name|serviceName
argument_list|()
argument_list|)
operator|||
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|seiAnnotation
operator|.
name|endpointInterface
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|String
name|expString
init|=
name|BUNDLE
operator|.
name|getString
argument_list|(
literal|"ILLEGAL_ATTRIBUTE_IN_SEI_ANNOTATION_EXC"
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|expString
argument_list|)
throw|;
block|}
name|wsAnnotations
operator|.
name|add
argument_list|(
name|seiAnnotation
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
name|implementorClass
operator|.
name|getInterfaces
argument_list|()
operator|.
name|length
operator|-
literal|1
init|;
name|x
operator|>=
literal|0
condition|;
name|x
operator|--
control|)
block|{
if|if
condition|(
name|seiClass
operator|.
name|equals
argument_list|(
name|implementorClass
operator|.
name|getInterfaces
argument_list|()
index|[
name|x
index|]
argument_list|)
condition|)
block|{
name|Type
name|type
init|=
name|implementorClass
operator|.
name|getGenericInterfaces
argument_list|()
index|[
name|x
index|]
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|seiType
operator|=
operator|(
name|ParameterizedType
operator|)
name|type
expr_stmt|;
block|}
block|}
block|}
block|}
name|wsProviderAnnotation
operator|=
name|getWebServiceProviderAnnotation
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|WebServiceProvider
name|getWebServiceProviderAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|WebServiceProvider
name|ann
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|WebServiceProvider
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ann
condition|)
block|{
return|return
name|ann
return|;
block|}
else|else
block|{
if|if
condition|(
name|ifAnnotationLoadedByOtherClassLoader
argument_list|(
name|cls
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"WEBSERVICE_ANNOTATIONS_IS_LOADED_BY_OTHER_CLASSLOADER"
argument_list|,
name|WebServiceProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|inf
range|:
name|cls
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
literal|null
operator|!=
name|inf
operator|.
name|getAnnotation
argument_list|(
name|WebServiceProvider
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|inf
operator|.
name|getAnnotation
argument_list|(
name|WebServiceProvider
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|ifAnnotationLoadedByOtherClassLoader
argument_list|(
name|cls
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"WEBSERVICE_ANNOTATIONS_IS_LOADED_BY_OTHER_CLASSLOADER"
argument_list|,
name|WebServiceProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|getWebServiceProviderAnnotation
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isWebServiceProvider
parameter_list|()
block|{
return|return
name|Provider
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|implementorClass
argument_list|)
return|;
block|}
specifier|public
name|WebServiceProvider
name|getWsProvider
parameter_list|()
block|{
return|return
name|wsProviderAnnotation
return|;
block|}
specifier|public
name|Service
operator|.
name|Mode
name|getServiceMode
parameter_list|()
block|{
name|ServiceMode
name|m
init|=
name|implementorClass
operator|.
name|getAnnotation
argument_list|(
name|ServiceMode
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
operator|&&
name|m
operator|.
name|value
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|m
operator|.
name|value
argument_list|()
return|;
block|}
return|return
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getProviderParameterType
parameter_list|()
block|{
return|return
name|doGetProviderParameterType
argument_list|(
name|implementorClass
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|doGetProviderParameterType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
while|while
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|Type
name|intfTypes
index|[]
init|=
name|c
operator|.
name|getGenericInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|Type
name|t
range|:
name|intfTypes
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|JAXBEncoderDecoder
operator|.
name|getClassFromType
argument_list|(
name|t
argument_list|)
decl_stmt|;
if|if
condition|(
name|Provider
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
if|if
condition|(
name|Provider
operator|.
name|class
operator|==
name|clazz
condition|)
block|{
name|Type
name|paramTypes
index|[]
init|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|t
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
return|return
name|JAXBEncoderDecoder
operator|.
name|getClassFromType
argument_list|(
name|paramTypes
index|[
literal|0
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doGetProviderParameterType
argument_list|(
name|clazz
argument_list|)
return|;
block|}
block|}
block|}
name|c
operator|=
name|c
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getBindingType
parameter_list|()
block|{
name|BindingType
name|bType
init|=
name|implementorClass
operator|.
name|getAnnotation
argument_list|(
name|BindingType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bType
operator|!=
literal|null
condition|)
block|{
return|return
name|bType
operator|.
name|value
argument_list|()
return|;
block|}
return|return
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
return|;
block|}
block|}
end_class

end_unit

