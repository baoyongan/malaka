/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.zcbl.malaka.rpc.common.inter;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bridge
{

	public interface Iface
	{

		public RpcResponse remote(RpcRequest arg0) throws org.apache.thrift.TException;

	}

	public interface AsyncIface
	{

		public void remote(RpcRequest arg0,
				org.apache.thrift.async.AsyncMethodCallback<AsyncClient.remote_call> resultHandler)
				throws org.apache.thrift.TException;

	}

	public static class Client extends org.apache.thrift.TServiceClient implements Iface
	{
		public static class Factory implements org.apache.thrift.TServiceClientFactory<Client>
		{
			public Factory()
			{
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol prot)
			{
				return new Client(prot);
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol iprot,
					org.apache.thrift.protocol.TProtocol oprot)
			{
				return new Client(iprot, oprot);
			}
		}

		public Client(org.apache.thrift.protocol.TProtocol prot)
		{
			super(prot, prot);
		}

		public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot)
		{
			super(iprot, oprot);
		}

		public RpcResponse remote(RpcRequest arg0) throws org.apache.thrift.TException
		{
			send_remote(arg0);
			return recv_remote();
		}

		public void send_remote(RpcRequest arg0) throws org.apache.thrift.TException
		{
			remote_args args = new remote_args();
			args.setArg0(arg0);
			sendBase("remote", args);
		}

		public RpcResponse recv_remote() throws org.apache.thrift.TException
		{
			remote_result result = new remote_result();
			receiveBase(result, "remote");
			if (result.isSetSuccess())
			{
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT,
					"remote failed: unknown result");
		}

	}

	public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface
	{
		public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient>
		{
			private org.apache.thrift.async.TAsyncClientManager clientManager;
			private org.apache.thrift.protocol.TProtocolFactory protocolFactory;

			public Factory(org.apache.thrift.async.TAsyncClientManager clientManager,
					org.apache.thrift.protocol.TProtocolFactory protocolFactory)
			{
				this.clientManager = clientManager;
				this.protocolFactory = protocolFactory;
			}

			public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport)
			{
				return new AsyncClient(protocolFactory, clientManager, transport);
			}
		}

		public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory,
				org.apache.thrift.async.TAsyncClientManager clientManager,
				org.apache.thrift.transport.TNonblockingTransport transport)
		{
			super(protocolFactory, clientManager, transport);
		}

		public void remote(RpcRequest arg0, org.apache.thrift.async.AsyncMethodCallback<remote_call> resultHandler)
				throws org.apache.thrift.TException
		{
			checkReady();
			remote_call method_call = new remote_call(arg0, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class remote_call extends org.apache.thrift.async.TAsyncMethodCall
		{
			private RpcRequest arg0;

			public remote_call(RpcRequest arg0, org.apache.thrift.async.AsyncMethodCallback<remote_call> resultHandler,
					org.apache.thrift.async.TAsyncClient client,
					org.apache.thrift.protocol.TProtocolFactory protocolFactory,
					org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException
			{
				super(client, protocolFactory, transport, resultHandler, false);
				this.arg0 = arg0;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException
			{
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("remote",
						org.apache.thrift.protocol.TMessageType.CALL, 0));
				remote_args args = new remote_args();
				args.setArg0(arg0);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public RpcResponse getResult() throws org.apache.thrift.TException
			{
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ)
				{
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(
						getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_remote();
			}
		}

	}

	public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I>
			implements org.apache.thrift.TProcessor
	{
		private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

		public Processor(I iface)
		{
			super(iface, getProcessMap(
					new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
		}

		protected Processor(I iface,
				Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap)
		{
			super(iface, getProcessMap(processMap));
		}

		private static <I extends Iface> Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(
				Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap)
		{
			processMap.put("remote", new remote());
			return processMap;
		}

		private static class remote<I extends Iface> extends org.apache.thrift.ProcessFunction<I, remote_args>
		{
			public remote()
			{
				super("remote");
			}

			public remote_args getEmptyArgsInstance()
			{
				return new remote_args();
			}

			public remote_result getResult(I iface, remote_args args) throws org.apache.thrift.TException
			{
				remote_result result = new remote_result();
				result.success = iface.remote(args.arg0);
				return result;
			}

			@Override
			protected boolean isOneway()
			{
				// TODO Auto-generated method stub
				return false;
			}
		}

	}

	public static class remote_args
			implements org.apache.thrift.TBase<remote_args, remote_args._Fields>, java.io.Serializable, Cloneable
	{
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"remote_args");

		private static final org.apache.thrift.protocol.TField ARG0_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"arg0", org.apache.thrift.protocol.TType.STRUCT, (short) 1);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static
		{
			schemes.put(StandardScheme.class, new remote_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new remote_argsTupleSchemeFactory());
		}

		public RpcRequest arg0; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum
		{
			ARG0((short) 1, "arg0");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static
			{
				for (_Fields field : EnumSet.allOf(_Fields.class))
				{
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId)
			{
				switch (fieldId)
				{
				case 1: // ARG0
					return ARG0;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId)
			{
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name)
			{
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName)
			{
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId()
			{
				return _thriftId;
			}

			public String getFieldName()
			{
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static
		{
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.ARG0,
					new org.apache.thrift.meta_data.FieldMetaData("arg0",
							org.apache.thrift.TFieldRequirementType.DEFAULT,
							new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT,
									RpcRequest.class)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(remote_args.class, metaDataMap);
		}

		public remote_args()
		{
		}

		public remote_args(RpcRequest arg0)
		{
			this();
			this.arg0 = arg0;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public remote_args(remote_args other)
		{
			if (other.isSetArg0())
			{
				this.arg0 = new RpcRequest(other.arg0);
			}
		}

		public remote_args deepCopy()
		{
			return new remote_args(this);
		}

		@Override
		public void clear()
		{
			this.arg0 = null;
		}

		public RpcRequest getArg0()
		{
			return this.arg0;
		}

		public remote_args setArg0(RpcRequest arg0)
		{
			this.arg0 = arg0;
			return this;
		}

		public void unsetArg0()
		{
			this.arg0 = null;
		}

		/**
		 * Returns true if field arg0 is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetArg0()
		{
			return this.arg0 != null;
		}

		public void setArg0IsSet(boolean value)
		{
			if (!value)
			{
				this.arg0 = null;
			}
		}

		public void setFieldValue(_Fields field, Object value)
		{
			switch (field)
			{
			case ARG0:
				if (value == null)
				{
					unsetArg0();
				} else
				{
					setArg0((RpcRequest) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field)
		{
			switch (field)
			{
			case ARG0:
				return getArg0();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field)
		{
			if (field == null)
			{
				throw new IllegalArgumentException();
			}

			switch (field)
			{
			case ARG0:
				return isSetArg0();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that)
		{
			if (that == null)
				return false;
			if (that instanceof remote_args)
				return this.equals((remote_args) that);
			return false;
		}

		public boolean equals(remote_args that)
		{
			if (that == null)
				return false;

			boolean this_present_arg0 = true && this.isSetArg0();
			boolean that_present_arg0 = true && that.isSetArg0();
			if (this_present_arg0 || that_present_arg0)
			{
				if (!(this_present_arg0 && that_present_arg0))
					return false;
				if (!this.arg0.equals(that.arg0))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode()
		{
			return 0;
		}

		public int compareTo(remote_args other)
		{
			if (!getClass().equals(other.getClass()))
			{
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;
			remote_args typedOther = (remote_args) other;

			lastComparison = Boolean.valueOf(isSetArg0()).compareTo(typedOther.isSetArg0());
			if (lastComparison != 0)
			{
				return lastComparison;
			}
			if (isSetArg0())
			{
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.arg0, typedOther.arg0);
				if (lastComparison != 0)
				{
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId)
		{
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException
		{
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException
		{
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("remote_args(");
			boolean first = true;

			sb.append("arg0:");
			if (this.arg0 == null)
			{
				sb.append("null");
			} else
			{
				sb.append(this.arg0);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException
		{
			// check for required fields
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
		{
			try
			{
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te)
			{
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException
		{
			try
			{
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te)
			{
				throw new java.io.IOException(te);
			}
		}

		private static class remote_argsStandardSchemeFactory implements SchemeFactory
		{
			public remote_argsStandardScheme getScheme()
			{
				return new remote_argsStandardScheme();
			}
		}

		private static class remote_argsStandardScheme extends StandardScheme<remote_args>
		{

			public void read(org.apache.thrift.protocol.TProtocol iprot, remote_args struct)
					throws org.apache.thrift.TException
			{
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true)
				{
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP)
					{
						break;
					}
					switch (schemeField.id)
					{
					case 1: // ARG0
						if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT)
						{
							struct.arg0 = new RpcRequest();
							struct.arg0.read(iprot);
							struct.setArg0IsSet(true);
						} else
						{
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, remote_args struct)
					throws org.apache.thrift.TException
			{
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.arg0 != null)
				{
					oprot.writeFieldBegin(ARG0_FIELD_DESC);
					struct.arg0.write(oprot);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class remote_argsTupleSchemeFactory implements SchemeFactory
		{
			public remote_argsTupleScheme getScheme()
			{
				return new remote_argsTupleScheme();
			}
		}

		private static class remote_argsTupleScheme extends TupleScheme<remote_args>
		{

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, remote_args struct)
					throws org.apache.thrift.TException
			{
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetArg0())
				{
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetArg0())
				{
					struct.arg0.write(oprot);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, remote_args struct)
					throws org.apache.thrift.TException
			{
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0))
				{
					struct.arg0 = new RpcRequest();
					struct.arg0.read(iprot);
					struct.setArg0IsSet(true);
				}
			}
		}

	}

	public static class remote_result
			implements org.apache.thrift.TBase<remote_result, remote_result._Fields>, java.io.Serializable, Cloneable
	{
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
				"remote_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField(
				"success", org.apache.thrift.protocol.TType.STRUCT, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static
		{
			schemes.put(StandardScheme.class, new remote_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new remote_resultTupleSchemeFactory());
		}

		public RpcResponse success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum
		{
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static
			{
				for (_Fields field : EnumSet.allOf(_Fields.class))
				{
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId)
			{
				switch (fieldId)
				{
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId)
			{
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name)
			{
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName)
			{
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId()
			{
				return _thriftId;
			}

			public String getFieldName()
			{
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static
		{
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
					_Fields.class);
			tmpMap.put(_Fields.SUCCESS,
					new org.apache.thrift.meta_data.FieldMetaData("success",
							org.apache.thrift.TFieldRequirementType.DEFAULT,
							new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT,
									RpcResponse.class)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(remote_result.class, metaDataMap);
		}

		public remote_result()
		{
		}

		public remote_result(RpcResponse success)
		{
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public remote_result(remote_result other)
		{
			if (other.isSetSuccess())
			{
				this.success = new RpcResponse(other.success);
			}
		}

		public remote_result deepCopy()
		{
			return new remote_result(this);
		}

		@Override
		public void clear()
		{
			this.success = null;
		}

		public RpcResponse getSuccess()
		{
			return this.success;
		}

		public remote_result setSuccess(RpcResponse success)
		{
			this.success = success;
			return this;
		}

		public void unsetSuccess()
		{
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess()
		{
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value)
		{
			if (!value)
			{
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value)
		{
			switch (field)
			{
			case SUCCESS:
				if (value == null)
				{
					unsetSuccess();
				} else
				{
					setSuccess((RpcResponse) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field)
		{
			switch (field)
			{
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field)
		{
			if (field == null)
			{
				throw new IllegalArgumentException();
			}

			switch (field)
			{
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that)
		{
			if (that == null)
				return false;
			if (that instanceof remote_result)
				return this.equals((remote_result) that);
			return false;
		}

		public boolean equals(remote_result that)
		{
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success)
			{
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode()
		{
			return 0;
		}

		public int compareTo(remote_result other)
		{
			if (!getClass().equals(other.getClass()))
			{
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;
			remote_result typedOther = (remote_result) other;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(typedOther.isSetSuccess());
			if (lastComparison != 0)
			{
				return lastComparison;
			}
			if (isSetSuccess())
			{
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, typedOther.success);
				if (lastComparison != 0)
				{
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId)
		{
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException
		{
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException
		{
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("remote_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null)
			{
				sb.append("null");
			} else
			{
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException
		{
			// check for required fields
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
		{
			try
			{
				write(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te)
			{
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException
		{
			try
			{
				read(new org.apache.thrift.protocol.TCompactProtocol(
						new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te)
			{
				throw new java.io.IOException(te);
			}
		}

		private static class remote_resultStandardSchemeFactory implements SchemeFactory
		{
			public remote_resultStandardScheme getScheme()
			{
				return new remote_resultStandardScheme();
			}
		}

		private static class remote_resultStandardScheme extends StandardScheme<remote_result>
		{

			public void read(org.apache.thrift.protocol.TProtocol iprot, remote_result struct)
					throws org.apache.thrift.TException
			{
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true)
				{
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP)
					{
						break;
					}
					switch (schemeField.id)
					{
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT)
						{
							struct.success = new RpcResponse();
							struct.success.read(iprot);
							struct.setSuccessIsSet(true);
						} else
						{
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, remote_result struct)
					throws org.apache.thrift.TException
			{
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null)
				{
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					struct.success.write(oprot);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class remote_resultTupleSchemeFactory implements SchemeFactory
		{
			public remote_resultTupleScheme getScheme()
			{
				return new remote_resultTupleScheme();
			}
		}

		private static class remote_resultTupleScheme extends TupleScheme<remote_result>
		{

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, remote_result struct)
					throws org.apache.thrift.TException
			{
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess())
				{
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess())
				{
					struct.success.write(oprot);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, remote_result struct)
					throws org.apache.thrift.TException
			{
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0))
				{
					struct.success = new RpcResponse();
					struct.success.read(iprot);
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

}